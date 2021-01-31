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

import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.MenuEntryDescriptor;
import org.drombler.acp.core.commons.util.BundleUtils;
import org.drombler.acp.core.commons.util.UnresolvedEntry;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.acp.core.docking.spi.DockingDescriptorUtils;
import org.drombler.acp.core.docking.spi.ViewDockingDescriptor;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockablePreferences;
import org.drombler.commons.docking.DockingAreaDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softsmithy.lib.util.SetChangeEvent;
import org.softsmithy.lib.util.SetChangeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * TODO: thread-safe???
 *
 * @author puce
 */
@Component(immediate = true)
public class ViewDockingHandler<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> extends AbstractDockableDockingHandler<D, DATA, E> {

    private static final Logger LOG = LoggerFactory.getLogger(ViewDockingHandler.class);

    private Executor applicationExecutor;
    private ViewDockingManager<D, DATA, E> viewDockingManager;
    private final Map<String, List<UnresolvedEntry<ViewDockingDescriptor<D, DATA, E>>>> unresolvedDockingDescriptorsAreaId = new HashMap<>();
    private final List<UnresolvedEntry<ViewDockingDescriptor<D, DATA, E>>> unresolvedDockingDescriptors = new ArrayList<>();
    private final Map<String, ServiceRegistration<ActionDescriptor>> actionServiceRegistrations = new HashMap<>();
    private final Map<String, ServiceRegistration<MenuEntryDescriptor>> menuEntryServiceRegistrations = new HashMap<>();
    private final SetChangeListener<DockingAreaDescriptor> dockingAreaListener = new DockingAreaListener();
//    private final BundleScope scope = new BundleScope();

    @Reference
    protected void bindApplicationThreadExecutorProvider(ApplicationThreadExecutorProvider applicationThreadExecutorProvider) {
        applicationExecutor = applicationThreadExecutorProvider.getApplicationThreadExecutor();
    }

    protected void unbindApplicationThreadExecutorProvider(ApplicationThreadExecutorProvider applicationThreadExecutorProvider) {
        applicationExecutor = null;
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindViewDockingDescriptor(ServiceReference<ViewDockingDescriptor<D, DATA, E>> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ViewDockingDescriptor<D, DATA, E> dockingDescriptor = context.getService(serviceReference);
        resolveDockable(dockingDescriptor, context);
    }

    protected void unbindViewDockingDescriptor(ViewDockingDescriptor<D, DATA, E> dockingDescriptor) throws InterruptedException {
        unregisterView(dockingDescriptor.getId(), dockingDescriptor.getDockableClass());
    }

    @Activate
    protected void activate(ComponentContext context) {
        getDockingAreaContainer().addDockingAreaSetChangeListener(dockingAreaListener);
        viewDockingManager = new ViewDockingManager<>(getDockingAreaContainer());
        resolveUnresolvedDockables();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) throws InterruptedException {
        getDockingAreaContainer().removeDockingAreaSetChangeListener(dockingAreaListener);
        unresolvedDockingDescriptors.clear();
        unresolvedDockingDescriptorsAreaId.clear();
//        ViewDockingManager<D, DATA, E> manager = viewDockingManager;
//        applicationExecutor.execute(() -> manager.close());
        viewDockingManager = null;
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && applicationExecutor != null;
    }

    @Override
    protected void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context) {
        dockingsType.getViewDocking().forEach(dockingType -> {
            try {
                ViewDockingDescriptor<?, ?, ?> dockingDescriptor = DockingDescriptorUtils.createViewDockingDescriptor(dockingType, bundle);
                // TODO: register ViewDockingDescriptor as service? Omit resolveDockable?
                resolveDockable((ViewDockingDescriptor<D, DATA, E>) dockingDescriptor, context);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | RuntimeException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }

    private void resolveDockable(final ViewDockingDescriptor<D, DATA, E> dockingDescriptor, final BundleContext context) {
        LOG.debug("Resolving {}...", dockingDescriptor);
        if (isInitialized()) {
            registerDefaultDockablePreferences(dockingDescriptor);
            applicationExecutor.execute(() -> addDockable(dockingDescriptor, context));
        } else {
            unresolvedDockingDescriptors.add(new UnresolvedEntry<>(dockingDescriptor, context));
        }
    }

    private void addDockable(final ViewDockingDescriptor<D, DATA, E> dockingDescriptor, final BundleContext context) {
        if (viewDockingManager.addView(dockingDescriptor)) {
            ServiceRegistration<ActionDescriptor> actionServiceRegistration = context.registerService(ActionDescriptor.class,
                    dockingDescriptor.getActivateDockableActionDescriptor(), null);
            actionServiceRegistrations.put(dockingDescriptor.getId(), actionServiceRegistration);
            ServiceRegistration<MenuEntryDescriptor> menuEntryServiceRegistration = context.registerService(MenuEntryDescriptor.class,
                    dockingDescriptor.getActivateDockableMenuEntryDescriptor(), null);
            menuEntryServiceRegistrations.put(dockingDescriptor.getId(), menuEntryServiceRegistration);
            LOG.debug("Resolved {}...", dockingDescriptor);
        } else {
            // TODO: Does this still work?
            final String areaId = dockingDescriptor.getAreaId();
            if (!unresolvedDockingDescriptorsAreaId.containsKey(areaId)) {
                unresolvedDockingDescriptorsAreaId.put(areaId, new ArrayList<>());
            }
            unresolvedDockingDescriptorsAreaId.get(areaId).add(new UnresolvedEntry<>(dockingDescriptor, context));
        }
    }

    private void addDockables(final List<UnresolvedEntry<ViewDockingDescriptor<D, DATA, E>>> unresolvedDockingDescriptors) {
        applicationExecutor.execute(()
                -> unresolvedDockingDescriptors.forEach(unresolvedEntry
                        -> addDockable(unresolvedEntry.getEntry(), unresolvedEntry.getContext()))
        );
    }

    private void registerDefaultDockablePreferences(ViewDockingDescriptor<D, DATA, E> dockingDescriptor) {
        DockablePreferences dockablePreferences = new DockablePreferences(dockingDescriptor.getAreaId(), dockingDescriptor.getPosition());
        registerDefaultDockablePreferences(dockingDescriptor.getDockableClass(), dockablePreferences);
    }

    private void registerDefaultDockablePreferences(List<UnresolvedEntry<ViewDockingDescriptor<D, DATA, E>>> unresolvedDockingDescriptors) {
        unresolvedDockingDescriptors.forEach(unresolvedEntry -> registerDefaultDockablePreferences(unresolvedEntry.getEntry()));
    }

    private void resolveUnresolvedDockables() {
        if (isInitialized()) {
            List<UnresolvedEntry<ViewDockingDescriptor<D, DATA, E>>> unresolvedDockingDescriptorsCopy
                    = new ArrayList<>(unresolvedDockingDescriptors);
            unresolvedDockingDescriptors.clear();
            registerDefaultDockablePreferences(unresolvedDockingDescriptorsCopy);
            addDockables(unresolvedDockingDescriptorsCopy);
        }
    }

    @Override
    protected void unregisterDockingsType(DockingsType dockingsType, Bundle bundle) throws InterruptedException {
        dockingsType.getViewDocking().forEach(dockingType -> {
            try {
                // TODO: unregister ViewDockingDescriptor as service? Omit resolveDockable?
                unregisterView(dockingType.getId(), BundleUtils.loadClass(bundle, dockingType.getDockableClass()));
            } catch (ClassNotFoundException | RuntimeException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }

    private void unregisterView(String viewId, Class<?> dockableClass) {
        if (isInitialized()) {
            unregisterDefaultDockablePreferences(dockableClass);
            removeView(viewId);
        } else {
            unresolvedDockingDescriptors.removeIf(unresolvedEntry -> unresolvedEntry.getEntry().getId().equals(viewId));
        }
    }

    private void removeView(String viewId) {
        if (menuEntryServiceRegistrations.containsKey(viewId)) {
            ServiceRegistration<MenuEntryDescriptor> menuEntryServiceRegistration = menuEntryServiceRegistrations.remove(viewId);
            menuEntryServiceRegistration.unregister();
        }
        if (actionServiceRegistrations.containsKey(viewId)) {
            ServiceRegistration<ActionDescriptor> actionServiceRegistration = actionServiceRegistrations.remove(viewId);
            actionServiceRegistration.unregister();
        }
        unresolvedDockingDescriptorsAreaId.entrySet().forEach(entry
                -> entry.getValue().removeIf(unresolvedEntry -> unresolvedEntry.getEntry().getId().equals(viewId)));
        applicationExecutor.execute(() -> {
            try {
                viewDockingManager.removeView(viewId);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }

    private void resolveUnresolvedDockables(String areaId) {
        if (unresolvedDockingDescriptorsAreaId.containsKey(areaId)) {
            applicationExecutor.execute(()
                    -> unresolvedDockingDescriptorsAreaId.get(areaId).forEach(unresolvedEntry -> addDockable(unresolvedEntry.getEntry(), unresolvedEntry.getContext()))
            );
        }
    }

    private class DockingAreaListener implements SetChangeListener<DockingAreaDescriptor> {

        /**
         * This method gets called from the application thread!
         *
         * @param event
         */
        @Override
        public void elementAdded(SetChangeEvent<DockingAreaDescriptor> event) {
            resolveUnresolvedDockables(event.getElement().getId());
        }

        /**
         * This method gets called from the application thread!
         *
         * @param event
         */
        @Override
        public void elementRemoved(SetChangeEvent<DockingAreaDescriptor> event) {
            // TODO: ???
        }

    }
}
