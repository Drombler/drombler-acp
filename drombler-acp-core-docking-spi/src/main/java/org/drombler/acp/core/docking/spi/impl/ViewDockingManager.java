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
import org.drombler.acp.core.docking.spi.DockableFactory;
import org.drombler.acp.core.docking.spi.ViewDockingDescriptor;
import org.drombler.commons.client.docking.Dockable;
import org.drombler.commons.client.docking.DockableEntry;
import org.drombler.commons.client.docking.DockablePreferences;
import org.drombler.commons.client.docking.DockablePreferencesManager;
import org.drombler.acp.core.docking.spi.DockingAreaContainer;
import org.drombler.acp.core.docking.spi.DockingAreaContainerDockingAreaEvent;
import org.drombler.acp.core.docking.spi.DockingAreaContainerListener;
import org.drombler.commons.context.ActiveContextProvider;
import org.drombler.commons.context.ActiveContextSensitive;
import org.drombler.commons.context.ApplicationContextProvider;
import org.drombler.commons.context.ApplicationContextSensitive;
import org.osgi.framework.BundleContext;

/**
 *
 * @author puce
 */
    // access only on the application thread
public class ViewDockingManager<D extends Dockable> implements AutoCloseable {

    private final Map<String, List<UnresolvedEntry<ViewDockingDescriptor>>> unresolvedDockingDescriptorsAreaId = new HashMap<>();
    private final DockingAreaListener dockingAreaListener = new DockingAreaListener();
    private final ActiveContextProvider activeContextProvider;
    private final ApplicationContextProvider applicationContextProvider;
    private final DockableFactory<D> dockableFactory;
    private final DockingAreaContainer<D> dockingAreaContainer;
    private final DockablePreferencesManager<D> dockablePreferencesManager;

    public ViewDockingManager(DockableFactory<D> dockableFactory, ActiveContextProvider activeContextProvider,
            ApplicationContextProvider applicationContextProvider, DockingAreaContainer<D> dockingAreaContainer,
            DockablePreferencesManager<D> dockablePreferencesManager) {
        this.dockableFactory = dockableFactory;
        this.activeContextProvider = activeContextProvider;
        this.applicationContextProvider = applicationContextProvider;
        this.dockingAreaContainer = dockingAreaContainer;
        this.dockablePreferencesManager = dockablePreferencesManager;

        this.dockingAreaContainer.addDockingAreaContainerListener(dockingAreaListener);
    }

    /**
     * Only call this method on the applicatio thread!
     *
     * @param dockingDescriptor
     * @param context
     */
    public void addDockable(final ViewDockingDescriptor dockingDescriptor,
            final BundleContext context) {
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
            DockablePreferences dockablePreferences = dockablePreferencesManager.getDockablePreferences(
                    dockable);
            if (dockingAreaContainer.addDockable(new DockableEntry<>(dockable, dockablePreferences))) {
                dockingDescriptor.getActivateDockableActionDescriptor().setListener(new ActivateDockableAction(
                        dockable));
                context
                        .registerService(ActionDescriptor.class,
                                dockingDescriptor.getActivateDockableActionDescriptor(),
                                null);
                context.registerService(MenuEntryDescriptor.class,
                        dockingDescriptor.getActivateDockableMenuEntryDescriptor(), null);
            } else {
                if (!unresolvedDockingDescriptorsAreaId.containsKey(dockablePreferences.getAreaId())) {
                    unresolvedDockingDescriptorsAreaId.put(dockablePreferences.getAreaId(),
                            new ArrayList<UnresolvedEntry<ViewDockingDescriptor>>());
                }
                unresolvedDockingDescriptorsAreaId.get(dockablePreferences.getAreaId()).add(new UnresolvedEntry<>(
                        dockingDescriptor, context));
            }
        }
    }

    private void resolveUnresolvedDockables(String areaId) {
        if (unresolvedDockingDescriptorsAreaId.containsKey(areaId)) {
            for (UnresolvedEntry<ViewDockingDescriptor> unresolvedEntry : unresolvedDockingDescriptorsAreaId.get(areaId)) {
                addDockable(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
            }
        }
    }

    @Override
    public void close() {
        dockingAreaContainer.removeDockingAreaContainerListener(dockingAreaListener);
    }

    private class DockingAreaListener<D> implements DockingAreaContainerListener<D> {

        /**
         * This method gets called from the application thread!
         *
         * @param event
         */
        @Override
        public void dockingAreaAdded(DockingAreaContainerDockingAreaEvent<D> event) {
            resolveUnresolvedDockables(event.getAreaId());
        }

        /**
         * This method gets called from the application thread!
         *
         * @param event
         */
        @Override
        public void dockingAreaRemoved(DockingAreaContainerDockingAreaEvent<D> event) {
            // TODO: ???
        }

    }
}
