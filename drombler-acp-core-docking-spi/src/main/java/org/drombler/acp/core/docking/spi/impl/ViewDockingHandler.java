/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.MenuEntryDescriptor;
import org.drombler.acp.core.application.ApplicationExecutorProvider;
import org.drombler.acp.core.docking.Dockable;
import org.drombler.acp.core.docking.spi.DockableFactory;
import org.drombler.acp.core.docking.spi.ViewDockingDescriptor;
import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.acp.core.docking.jaxb.ViewDockingType;

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
public class ViewDockingHandler<A, D extends Dockable> extends AbstractDockableDockingHandler<A, D> {

    @Reference
    private DockableFactory<D> dockableFactory;
    private Executor applicationExecutor;
    private final List<UnresolvedEntry<ViewDockingDescriptor>> unresolvedDockingDescriptors = new ArrayList<>();

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
                        dockingDescriptor.getActivateDockableActionDescriptor().setListener(new ActivateDockableAction(
                                dockable));
                        registerDockablePreferences(dockable.getClass(), dockingDescriptor.getAreaId(),
                                dockingDescriptor.getPosition());
                        getDockingAreaContainerProvider().getDockingAreaContainer().addDockable(dockable);
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
