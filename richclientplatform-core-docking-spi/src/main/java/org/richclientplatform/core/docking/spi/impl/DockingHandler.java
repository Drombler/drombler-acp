/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.spi.impl;

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
import org.richclientplatform.core.action.spi.ActionDescriptor;
import org.richclientplatform.core.action.spi.MenuEntryDescriptor;
import org.richclientplatform.core.application.ApplicationExecutorProvider;
import org.richclientplatform.core.docking.jaxb.DockingType;
import org.richclientplatform.core.docking.jaxb.DockingsType;
import org.richclientplatform.core.docking.spi.DockableFactory;
import org.richclientplatform.core.docking.spi.DockablePreferences;
import org.richclientplatform.core.docking.spi.DockablePreferencesManager;
import org.richclientplatform.core.docking.spi.DockingDescriptor;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "dockingsType", referenceInterface = DockingsType.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "dockingDescriptor", referenceInterface = DockingDescriptor.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "applicationExecutorProvider", referenceInterface = ApplicationExecutorProvider.class)
})
public class DockingHandler<A, D> extends AbstractDockingHandler<A, D> {

    @Reference
    private DockableFactory<D> dockableFactory;
    @Reference
    private DockablePreferencesManager<D> dockablePreferencesManager;
    private Executor applicationExecutor;
    private final List<UnresolvedEntry<DockingDescriptor>> unresolvedDockingDescriptors = new ArrayList<>();

    protected void bindDockableFactory(DockableFactory<D> dockableFactory) {
        this.dockableFactory = dockableFactory;
    }

    protected void unbindDockableFactory(DockableFactory<D> dockableFactory) {
        this.dockableFactory = null;
    }

    protected void bindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = applicationExecutorProvider.getApplicationExecutor();
    }

    protected void unbindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = null;
    }

    protected void bindDockablePreferencesManager(DockablePreferencesManager<D> dockablePreferencesManager) {
        this.dockablePreferencesManager = dockablePreferencesManager;
    }

    protected void unbindDockablePreferencesManager(DockablePreferencesManager dockablePreferencesManager) {
        this.dockablePreferencesManager = null;
    }

    protected void bindDockingsType(ServiceReference<DockingsType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        DockingsType dockingsType = context.getService(serviceReference);
        for (DockingType dockingType : dockingsType.getDocking()) {
            try {
                DockingDescriptor dockingDescriptor = DockingDescriptor.createDockingDescriptor(dockingType, bundle);
                resolveDockable(dockingDescriptor, context);
            } catch (Exception ex) {
                Logger.getLogger(DockingHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void unbindDockingsType(DockingsType dockingAreasType) {
        // TODO
    }

    protected void bindDockingDescriptor(ServiceReference<DockingDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        DockingDescriptor dockingDescriptor = context.getService(serviceReference);
        resolveDockable(dockingDescriptor, context);
    }

    protected void unbindDockingDescriptor(DockingDescriptor dockingDescriptor) {
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
        return super.isInitialized() && dockableFactory != null && dockablePreferencesManager != null && applicationExecutor != null;
    }

    private void resolveDockable(final DockingDescriptor dockingDescriptor, final BundleContext context) {
        if (isInitialized()) {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    D dockable = dockableFactory.createDockable(dockingDescriptor);
                    registerDockablePreferences(dockable, dockingDescriptor);
                    getDockingAreaContainer().addDockable(dockable);
                    context.registerService(ActionDescriptor.class,
                            dockingDescriptor.getActivateDockableActionDescriptor(),
                            null);
                    context.registerService(MenuEntryDescriptor.class,
                            dockingDescriptor.getActivateDockableMenuEntryDescriptor(), null);
                }
            };
            applicationExecutor.execute(runnable);
        } else {
            unresolvedDockingDescriptors.add(new UnresolvedEntry<>(dockingDescriptor, context));
        }
    }

    private void registerDockablePreferences(D dockable, DockingDescriptor dockingDescriptor) {
        DockablePreferences dockablePreferences = new DockablePreferences();
        dockablePreferences.setAreaId(dockingDescriptor.getAreaId());
        dockablePreferences.setPosition(dockingDescriptor.getPosition());
        dockablePreferencesManager.registerDockablePreferences(dockable, dockablePreferences);
    }

    private void resolveUnresolvedDockables() {
        for (UnresolvedEntry<DockingDescriptor> unresolvedEntry : unresolvedDockingDescriptors) {
            resolveDockable(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
        }
    }
}
