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
 * Copyright 2014 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.docking.cdi.extension.impl;

import org.drombler.acp.core.docking.spi.DockableDataFactory;
import org.drombler.acp.core.docking.spi.DockableEntryFactory;
import org.drombler.acp.core.docking.spi.DockableFactory;
import org.drombler.acp.core.docking.spi.DockingAreaContainer;
import org.drombler.commons.client.docking.DockableData;
import org.drombler.commons.client.docking.DockableDataManager;
import org.drombler.commons.client.docking.DockableEntry;
import org.drombler.commons.client.docking.DockablePreferencesManager;
import org.drombler.commons.client.docking.DockingInjector;
import org.drombler.commons.context.ContextInjector;

/**
 *
 * @author puce
 */
    // access only on the application thread
public class ViewDockingManager<D, DATA extends DockableData, E extends DockableEntry<D>> implements AutoCloseable {

//    private final Map<String, List<UnresolvedEntry<ViewDockingDescriptor>>> unresolvedDockingDescriptorsAreaId = new HashMap<>();
//    private final DockingAreaListener<D, E> dockingAreaListener = new DockingAreaListener<>();
    private final DockableFactory<D> dockableFactory;
    private final DockableDataFactory<DATA> dockableDataFactory;
    private final DockableEntryFactory<D, E> dockableEntryFactory;
    private final ContextInjector contextInjector;
    private final DockingInjector<D, DATA> dockingInjector;
    private final DockingAreaContainer<D, E> dockingAreaContainer;
    private final DockableDataManager<D, DATA> dockableDataManager;
    private final DockablePreferencesManager<D> dockablePreferencesManager;

    public ViewDockingManager(DockableFactory<D> dockableFactory, DockableDataFactory<DATA> dockableDataFactory,
            DockableEntryFactory<D, E> dockableEntryFactory, ContextInjector contextInjector,
            DockingAreaContainer<D, E> dockingAreaContainer,
            DockableDataManager<D, DATA> dockableDataManager,
            DockablePreferencesManager<D> dockablePreferencesManager) {
        this.dockableFactory = dockableFactory;
        this.dockableDataFactory = dockableDataFactory;
        this.dockableEntryFactory = dockableEntryFactory;
        this.contextInjector = contextInjector;
        this.dockingAreaContainer = dockingAreaContainer;
        this.dockableDataManager = dockableDataManager;
        this.dockingInjector = new DockingInjector<>(dockableDataManager);
        this.dockablePreferencesManager = dockablePreferencesManager;

//        this.dockingAreaContainer.addDockingAreaContainerListener(dockingAreaListener);
    }

    /**
     * Only call this method on the applicatio thread!
     *
     * @param dockingDescriptor
     * @param context
     */
//    public void addDockable(final ViewDockingDescriptor dockingDescriptor,
//            final BundleContext context) {
//        final D dockable = dockableFactory.createDockable(dockingDescriptor);
//        if (dockable != null) {
//            final DATA dockableData = dockableDataFactory.createDockableData(dockingDescriptor);
//            dockableDataManager.registerDockableData(dockable, dockableData);
//
//            contextInjector.inject(dockable);
//            dockingInjector.inject(dockable);
//
//            final DockablePreferences dockablePreferences = dockablePreferencesManager.getDockablePreferences(
//                    dockable);
//            if (dockingAreaContainer.addDockable(dockableEntryFactory.createDockableEntry(dockable, dockablePreferences))) {
//                dockingDescriptor.getActivateDockableActionDescriptor().setListener(new ActivateDockableAction<>(
//                        dockable));
//                context.registerService(ActionDescriptor.class,
//                        dockingDescriptor.getActivateDockableActionDescriptor(), null);
//                context.registerService(MenuEntryDescriptor.class,
//                        dockingDescriptor.getActivateDockableMenuEntryDescriptor(), null);
//            } else {
//                // TODO: Does this still work?
//                if (!unresolvedDockingDescriptorsAreaId.containsKey(dockablePreferences.getAreaId())) {
//                    unresolvedDockingDescriptorsAreaId.put(dockablePreferences.getAreaId(), new ArrayList<>());
//                }
//                unresolvedDockingDescriptorsAreaId.get(dockablePreferences.getAreaId()).add(new UnresolvedEntry<>(
//                        dockingDescriptor, context));
//            }
//        }
//    }

//    private void resolveUnresolvedDockables(String areaId) {
//        if (unresolvedDockingDescriptorsAreaId.containsKey(areaId)) {
//            unresolvedDockingDescriptorsAreaId.get(areaId).forEach((unresolvedEntry)
//                    -> addDockable(unresolvedEntry.getEntry(), unresolvedEntry.getContext()));
//        }
//    }

    @Override
    public void close() {
//        dockingAreaContainer.removeDockingAreaContainerListener(dockingAreaListener);
    }

//    private class DockingAreaListener<D, E extends DockableEntry<D>> implements DockingAreaContainerListener<D, E> {
//
//        /**
//         * This method gets called from the application thread!
//         *
//         * @param event
//         */
//        @Override
//        public void dockingAreaAdded(DockingAreaContainerDockingAreaEvent<D, E> event) {
//            resolveUnresolvedDockables(event.getAreaId());
//        }
//
//        /**
//         * This method gets called from the application thread!
//         *
//         * @param event
//         */
//        @Override
//        public void dockingAreaRemoved(DockingAreaContainerDockingAreaEvent<D, E> event) {
//            // TODO: ???
//        }
//
//    }
}
