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

import java.util.HashMap;
import java.util.Map;
import org.drombler.acp.core.docking.spi.ViewDockingDescriptor;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.context.DockingAreaContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
// access only on the application thread
public class ViewDockingManager<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> {

    private static final Logger LOG = LoggerFactory.getLogger(ViewDockingManager.class);

    private final Map<String, E> viewEntries = new HashMap<>();

    private final DockingAreaContainer<D, DATA, E> dockingAreaContainer;

    public ViewDockingManager(DockingAreaContainer<D, DATA, E> dockingAreaContainer) {
        this.dockingAreaContainer = dockingAreaContainer;
    }

    /**
     * Adds a view to the {@link DockingAreaContainer}.<br>
     * <br>
     * Note: Only call this method on the application thread!
     *
     * @param dockingDescriptor the view to add
     * @return true, if the view as added, else false
     */
    public boolean addView(final ViewDockingDescriptor<D, DATA, E> dockingDescriptor) {
        E viewEntry = dockingAreaContainer.openAndRegisterNewView(dockingDescriptor.getDockableClass(), false,
                dockingDescriptor.getDisplayName(), dockingDescriptor.getIcon(), dockingDescriptor.getResourceLoader());
        if (viewEntry != null) {
            dockingDescriptor.setViewEntry(viewEntry);
            viewEntries.put(dockingDescriptor.getId(), viewEntry);
            return true;
        } else {
            return false;
        }
    }

    public E removeView(String dockableId) throws Exception {
        if (viewEntries.containsKey(dockableId)) {
            E dockableEntry = viewEntries.remove(dockableId);
            dockingAreaContainer.closeAndUnregisterView(dockableEntry);
            return dockableEntry;
        }
        return null;
    }

//    @Override
//    public void close() {
//        // TODO: show with a unit test that this is needed to be executed on the applicatoin thread
//        Map<String, E> viewEntriesCopy = new HashMap<>(viewEntries);
//        viewEntriesCopy.entrySet().forEach(entry -> {
//            try {
//                E viewEntry = entry.getValue();
//                dockingAreaContainer.closeAndUnregisterView(viewEntry);
//            } catch (Exception ex) {
//                LOG.error(ex.getMessage(), ex);
//            }
//        });
//        viewEntries.clear();
//    }

}
