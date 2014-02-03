/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2012 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.docking.spi.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.MenuEntryDescriptor;
import org.drombler.acp.core.application.ApplicationExecutorProvider;
import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.acp.core.docking.jaxb.ViewDockingType;
import org.drombler.acp.core.docking.spi.DockableFactory;
import org.drombler.acp.core.docking.spi.ViewDockingDescriptor;
import org.drombler.commons.client.docking.Dockable;
import org.drombler.commons.client.docking.DockablePreferences;
import org.drombler.commons.context.ActiveContextProvider;
import org.drombler.commons.context.ActiveContextSensitive;
import org.drombler.commons.context.ApplicationContextProvider;
import org.drombler.commons.context.ApplicationContextSensitive;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "viewDockingDescriptor", referenceInterface = ViewDockingDescriptor.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "applicationExecutorProvider", referenceInterface = ApplicationExecutorProvider.class)
})
public class ViewDockingHandler<D extends Dockable> extends AbstractDockableDockingHandler<D> {

    @Reference
    private ActiveContextProvider activeContextProvider;
    @Reference
    private ApplicationContextProvider applicationContextProvider;
    @Reference
    private DockableFactory<D> dockableFactory;
    private Executor applicationExecutor;
    private final List<UnresolvedEntry<ViewDockingDescriptor>> unresolvedDockingDescriptors = new ArrayList<>();

    protected void bindActiveContextProvider(ActiveContextProvider activeContextProvider) {
        this.activeContextProvider = activeContextProvider;
    }

    protected void unbindActiveContextProvider(ActiveContextProvider activeContextProvider) {
        this.activeContextProvider = null;
    }

    protected void bindApplicationContextProvider(ApplicationContextProvider applicationContextProvider) {
        this.applicationContextProvider = applicationContextProvider;
    }

    protected void unbindApplicationContextProvider(ApplicationContextProvider applicationContextProvider) {
        this.applicationContextProvider = null;
    }

    protected void bindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = applicationExecutorProvider.getApplicationExecutor();
    }

    protected void unbindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = null;
    }

    protected void bindDockableFactory(DockableFactory<D> dockableFactory) {
        this.dockableFactory = dockableFactory;
    }

    protected void unbindDockableFactory(DockableFactory<D> dockableFactory) {
        this.dockableFactory = null;
    }

    protected void bindViewDockingDescriptor(ServiceReference<ViewDockingDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ViewDockingDescriptor dockingDescriptor = context.getService(serviceReference);
        resolveDockable(dockingDescriptor, context);
    }

    protected void unbindViewDockingDescriptor(ViewDockingDescriptor dockingDescriptor) {
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedDockables();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && dockableFactory != null && applicationExecutor != null;
    }

    @Override
    protected void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context) {
        for (ViewDockingType dockingType : dockingsType.getViewDocking()) {
            try {
                ViewDockingDescriptor dockingDescriptor = ViewDockingDescriptor.createViewDockingDescriptor(dockingType,
                        bundle);
                resolveDockable(dockingDescriptor, context);
            } catch (Exception ex) {
                Logger.getLogger(ViewDockingHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void resolveDockable(final ViewDockingDescriptor dockingDescriptor, final BundleContext context) {
        if (isInitialized()) {
            Runnable dockableRegistration = new Runnable() {
                @Override
                public void run() {
                    D dockable = dockableFactory.createDockable(dockingDescriptor);
                    if (dockable != null) {
                        if (dockable instanceof ActiveContextSensitive) {
                            ((ActiveContextSensitive) dockable).setActiveContext(
                                    activeContextProvider.getActiveContext());
                        }
                        if (dockable instanceof ApplicationContextSensitive) {
                            ((ApplicationContextSensitive) dockable).setApplicationContext(
                                    applicationContextProvider.getApplicationContext());
                        }
                        dockingDescriptor.getActivateDockableActionDescriptor().setListener(new ActivateDockableAction(
                                dockable));
                        DockablePreferences dockablePreferences = createDockablePreferences(dockingDescriptor.
                                getAreaId(), dockingDescriptor.getPosition());
                        registerDefaultDockablePreferences(dockable.getClass(), dockablePreferences);
                        getDockingAreaContainerProvider().getDockingAreaContainer().addDockable(dockable,
                                dockablePreferences);
                        context.registerService(ActionDescriptor.class,
                                dockingDescriptor.getActivateDockableActionDescriptor(),
                                null);
                        context.registerService(MenuEntryDescriptor.class,
                                dockingDescriptor.getActivateDockableMenuEntryDescriptor(), null);
                    }
                }
            };
            applicationExecutor.execute(dockableRegistration);
        } else {
            unresolvedDockingDescriptors.add(new UnresolvedEntry<>(dockingDescriptor, context));
        }
    }

    private void resolveUnresolvedDockables() {
        for (UnresolvedEntry<ViewDockingDescriptor> unresolvedEntry : unresolvedDockingDescriptors) {
            resolveDockable(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
        }
    }
}
