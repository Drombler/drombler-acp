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
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.drombler.acp.startup.main.ApplicationExecutorProvider;
import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.acp.core.docking.spi.DockableEntryFactory;
import org.drombler.acp.core.docking.spi.DockableFactory;
import org.drombler.acp.core.docking.spi.ViewDockingDescriptor;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockablePreferences;
import org.drombler.commons.context.ActiveContextProvider;
import org.drombler.commons.context.ApplicationContextProvider;
import org.drombler.commons.context.ContextInjector;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: thread-safe???
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "viewDockingDescriptor", referenceInterface = ViewDockingDescriptor.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "applicationExecutorProvider", referenceInterface = ApplicationExecutorProvider.class)
})
public class ViewDockingHandler<D, DATA extends DockableData, E extends DockableEntry<D>> extends AbstractDockableDockingHandler<D, DATA, E> {

    private static final Logger LOG = LoggerFactory.getLogger(ViewDockingHandler.class);

    @Reference
    private ActiveContextProvider activeContextProvider;
    @Reference
    private ApplicationContextProvider applicationContextProvider;
    @Reference
    private DockableFactory<D> dockableFactory;
    @Reference
    private DockableEntryFactory<D, E> dockableEntryFactory;
    private Executor applicationExecutor;
    private ViewDockingManager<D, DATA, E> viewDockingManager;
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

    protected void bindDockableEntryFactory(DockableEntryFactory<D, E> dockableEntryFactory) {
        this.dockableEntryFactory = dockableEntryFactory;
    }

    protected void unbindDockableEntryFactory(DockableEntryFactory<D, E> dockableEntryFactory) {
        this.dockableEntryFactory = null;
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
        viewDockingManager = new ViewDockingManager<>(dockableFactory, getDockableDataFactory(), dockableEntryFactory,
                new ContextInjector(activeContextProvider, applicationContextProvider),
                getDockingAreaContainerProvider().getDockingAreaContainer(), getDockableDataManager(),
                getDockablePreferencesManager());
        resolveUnresolvedDockables();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        viewDockingManager.close();
        viewDockingManager = null;
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && dockableFactory != null && dockableEntryFactory != null
                && applicationExecutor != null && activeContextProvider != null && applicationContextProvider != null;
    }

    @Override
    protected void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context) {
        dockingsType.getViewDocking().forEach(dockingType -> {
            try {
                ViewDockingDescriptor dockingDescriptor = ViewDockingDescriptor.createViewDockingDescriptor(dockingType,
                        bundle);
                // TODO: register ViewDockingDescriptor as service? Omit resolveDockable?
                resolveDockable(dockingDescriptor, context);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }

    private void resolveDockable(final ViewDockingDescriptor dockingDescriptor, final BundleContext context) {
        if (isInitialized()) {
            resolveDockableBasic(dockingDescriptor);
            addDockable(dockingDescriptor, context);
        } else {
            unresolvedDockingDescriptors.add(new UnresolvedEntry<>(dockingDescriptor, context));
        }
    }

    private void resolveDockableBasic(final ViewDockingDescriptor dockingDescriptor) {
        registerDefaultDockablePreferences(dockingDescriptor);
    }

    private void addDockable(final ViewDockingDescriptor dockingDescriptor, final BundleContext context) {
        applicationExecutor.execute(() -> viewDockingManager.addDockable(dockingDescriptor, context));
    }

    private void addDockables(final List<UnresolvedEntry<ViewDockingDescriptor>> unresolvedDockingDescriptors) {
        applicationExecutor.execute(()
                -> unresolvedDockingDescriptors.forEach(unresolvedEntry
                        -> viewDockingManager.addDockable(unresolvedEntry.getEntry(), unresolvedEntry.getContext()))
        );
    }

    private void registerDefaultDockablePreferences(ViewDockingDescriptor dockingDescriptor) {
        DockablePreferences dockablePreferences = createDockablePreferences(dockingDescriptor.
                getAreaId(), dockingDescriptor.getPosition());
        registerDefaultDockablePreferences(dockingDescriptor.getDockableClass(), dockablePreferences);
    }

    private void registerDefaultDockablePreferences(
            List<UnresolvedEntry<ViewDockingDescriptor>> unresolvedDockingDescriptors) {
        unresolvedDockingDescriptors.forEach(unresolvedEntry -> resolveDockableBasic(unresolvedEntry.getEntry()));
    }

    private void resolveUnresolvedDockables() {
        if (isInitialized()) {
            List<UnresolvedEntry<ViewDockingDescriptor>> unresolvedDockingDescriptorsCopy
                    = new ArrayList<>(unresolvedDockingDescriptors);
            unresolvedDockingDescriptors.clear();
            registerDefaultDockablePreferences(unresolvedDockingDescriptorsCopy);
            addDockables(unresolvedDockingDescriptorsCopy);
        }
    }

}
