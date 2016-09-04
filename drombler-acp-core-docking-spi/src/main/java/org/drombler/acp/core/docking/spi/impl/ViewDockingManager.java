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
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockingAreaDescriptor;
import org.drombler.commons.docking.context.DockingAreaContainer;
import org.osgi.framework.BundleContext;
import org.softsmithy.lib.util.SetChangeEvent;
import org.softsmithy.lib.util.SetChangeListener;

/**
 *
 * @author puce
 */
// access only on the application thread
public class ViewDockingManager<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> implements AutoCloseable {

    private final Map<String, List<UnresolvedEntry<ViewDockingDescriptor<? extends D>>>> unresolvedDockingDescriptorsAreaId = new HashMap<>();
    private final SetChangeListener<DockingAreaDescriptor> dockingAreaListener = new DockingAreaListener();
    private final DockableFactory<D> dockableFactory;
    private final DockingAreaContainer<D, DATA, E> dockingAreaContainer;

    public ViewDockingManager(DockableFactory<D> dockableFactory, DockingAreaContainer<D, DATA, E> dockingAreaContainer) {
        this.dockableFactory = dockableFactory;
        this.dockingAreaContainer = dockingAreaContainer;

        this.dockingAreaContainer.addDockingAreaSetChangeListener(dockingAreaListener);
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
            if (dockingAreaContainer.openAndRegisterNewView(dockable, false, dockingDescriptor.getDisplayName(), dockingDescriptor.getIcon(), dockingDescriptor.getResourceLoader())) {
                dockingDescriptor.setDockable(dockable);
                context.registerService(ActionDescriptor.class,
                        dockingDescriptor.getActivateDockableActionDescriptor(), null);
                context.registerService(MenuEntryDescriptor.class,
                        dockingDescriptor.getActivateDockableMenuEntryDescriptor(), null);
            } else {
                // TODO: Does this still work?
                final String preferredAreaId = dockingAreaContainer.getDockablePreferences(dockable).getAreaId();
                if (!unresolvedDockingDescriptorsAreaId.containsKey(preferredAreaId)) {
                    unresolvedDockingDescriptorsAreaId.put(preferredAreaId, new ArrayList<>());
                }
                unresolvedDockingDescriptorsAreaId.get(preferredAreaId).add(new UnresolvedEntry<>(dockingDescriptor, context));
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
        dockingAreaContainer.removeDockingAreaSetChangeListener(dockingAreaListener);
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
