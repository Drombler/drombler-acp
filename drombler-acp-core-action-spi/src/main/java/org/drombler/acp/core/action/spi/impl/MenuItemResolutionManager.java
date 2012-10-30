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
package org.drombler.acp.core.action.spi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.drombler.acp.core.action.spi.AbstractMenuEntryDescriptor;


/**
 *
 * @author puce
 */
public class MenuItemResolutionManager<D extends AbstractMenuEntryDescriptor> {

    private final Map<String, MenuItemResolutionManager<D>> unresolvedMenuItemContainers = new HashMap<>();
    private final Map<String, List<UnresolvedEntry<D>>> unresolvedMenuEntryDescriptors = new HashMap<>();


    public MenuItemResolutionManager<D> getMenuResolutionManager(String pathId) {
        if (!unresolvedMenuItemContainers.containsKey(pathId)) {
            unresolvedMenuItemContainers.put(pathId, new MenuItemResolutionManager<D>());
        }
        return unresolvedMenuItemContainers.get(pathId);
    }

    public void addUnresolvedMenuEntry(String pathId, UnresolvedEntry<D> unresolvedMenuEntry) {
        if (!unresolvedMenuEntryDescriptors.containsKey(pathId)) {
            unresolvedMenuEntryDescriptors.put(pathId, new ArrayList<UnresolvedEntry<D>>());
        }
        unresolvedMenuEntryDescriptors.get(pathId).add(unresolvedMenuEntry);
    }

    public List<UnresolvedEntry<D>> removeUnresolvedMenuEntries(String pathId) {
        return unresolvedMenuEntryDescriptors.remove(pathId);
    }

    public boolean containsUnresolvedMenuEntries(String pathId) {
        return unresolvedMenuEntryDescriptors.containsKey(pathId);
    }

}
