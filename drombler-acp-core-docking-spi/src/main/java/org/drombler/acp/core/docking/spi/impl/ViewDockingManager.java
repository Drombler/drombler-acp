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
package org.drombler.acp.core.docking.spi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.MenuEntryDescriptor;
import org.drombler.acp.core.docking.spi.DockableDataFactory;
import org.drombler.acp.core.docking.spi.DockableEntryFactory;
import org.drombler.acp.core.docking.spi.DockableFactory;
import org.drombler.acp.core.docking.spi.DockingAreaContainer;
import org.drombler.acp.core.docking.spi.DockingAreaContainerDockableEvent;
import org.drombler.acp.core.docking.spi.DockingAreaContainerDockingAreaEvent;
import org.drombler.acp.core.docking.spi.DockingAreaContainerListener;
import org.drombler.acp.core.docking.spi.ViewDockingDescriptor;
import org.drombler.commons.context.ContextInjector;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableDataManager;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockableKind;
import org.drombler.commons.docking.DockablePreferences;
import org.drombler.commons.docking.DockablePreferencesManager;
import org.drombler.commons.docking.DockingInjector;
import org.osgi.framework.BundleContext;

/**
 *
 * @author puce
 */
// access only on the application thread
public class ViewDockingManager<D, DATA extends DockableData, E extends DockableEntry<D>> implements AutoCloseable {

    private final Map<String, List<UnresolvedEntry<ViewDockingDescriptor<? extends D>>>> unresolvedDockingDescriptorsAreaId = new HashMap<>();
    private final DockingAreaListener<D, E> dockingAreaListener = new DockingAreaListener<>();
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

        this.dockingAreaContainer.addDockingAreaContainerListener(dockingAreaListener);
    }

    /**
     * Only call this method on the applicatio thread!
     *
     * @param dockingDescriptor
     * @param context
     */
    public <T extends D> void addDockable(final ViewDockingDescriptor<T> dockingDescriptor, final BundleContext context) {
        final T dockable = dockableFactory.createDockable(dockingDescriptor);
        if (dockable != null) {
            final DATA dockableData = dockableDataFactory.createDockableData(dockingDescriptor);
            dockableDataManager.registerDockableData(dockable, dockableData);

            contextInjector.inject(dockable);
            dockingInjector.inject(dockable);

            DockablePreferences dockablePreferences = dockablePreferencesManager.getDockablePreferences(dockable);
            if (dockingAreaContainer.addDockable(dockableEntryFactory.createDockableEntry(dockable, DockableKind.VIEW, dockablePreferences), false)) {
                dockingDescriptor.setDockable(dockable);
                context.registerService(ActionDescriptor.class,
                        dockingDescriptor.getActivateDockableActionDescriptor(), null);
                context.registerService(MenuEntryDescriptor.class,
                        dockingDescriptor.getActivateDockableMenuEntryDescriptor(), null);
            } else {
                // TODO: Does this still work?
                if (!unresolvedDockingDescriptorsAreaId.containsKey(dockablePreferences.getAreaId())) {
                    unresolvedDockingDescriptorsAreaId.put(dockablePreferences.getAreaId(), new ArrayList<>());
                }
                unresolvedDockingDescriptorsAreaId.get(dockablePreferences.getAreaId()).add(new UnresolvedEntry<>(dockingDescriptor, context));
            }
        }
    }

    private void resolveUnresolvedDockables(String areaId) {
        if (unresolvedDockingDescriptorsAreaId.containsKey(areaId)) {
            unresolvedDockingDescriptorsAreaId.get(areaId).forEach(unresolvedEntry
                    -> addDockable(unresolvedEntry.getEntry(), unresolvedEntry.getContext()));
        }
    }

    @Override
    public void close() {
        dockingAreaContainer.removeDockingAreaContainerListener(dockingAreaListener);
    }

    private class DockingAreaListener<D, E extends DockableEntry<D>> implements DockingAreaContainerListener<D, E> {

        /**
         * This method gets called from the application thread!
         *
         * @param event
         */
        @Override
        public void dockingAreaAdded(DockingAreaContainerDockingAreaEvent<D, E> event) {
            resolveUnresolvedDockables(event.getAreaId());
        }

        /**
         * This method gets called from the application thread!
         *
         * @param event
         */
        @Override
        public void dockingAreaRemoved(DockingAreaContainerDockingAreaEvent<D, E> event) {
            // TODO: ???
        }

        @Override
        public void dockableAdded(DockingAreaContainerDockableEvent<D, E> event) {
            // do nothing
        }

        @Override
        public void dockableRemoved(DockingAreaContainerDockableEvent<D, E> event) {
            // do nothing
        }

    }
}
