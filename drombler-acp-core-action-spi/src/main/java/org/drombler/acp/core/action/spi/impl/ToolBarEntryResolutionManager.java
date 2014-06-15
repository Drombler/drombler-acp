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
import org.drombler.acp.core.action.spi.ToolBarEntryDescriptor;

/**
 *
 * @author puce
 */
public class ToolBarEntryResolutionManager<D extends ToolBarEntryDescriptor> {

    private final Map<String, List<UnresolvedEntry<D>>> unresolvedToolBarEntryDescriptors = new HashMap<>();

    public void addUnresolvedToolBarEntry(UnresolvedEntry<D> unresolvedToolBarEntry) {
        if (!unresolvedToolBarEntryDescriptors.containsKey(unresolvedToolBarEntry.getEntry().getToolBarId())) {
            unresolvedToolBarEntryDescriptors.put(unresolvedToolBarEntry.getEntry().getToolBarId(),
                    new ArrayList<>());
        }
        unresolvedToolBarEntryDescriptors.get(unresolvedToolBarEntry.getEntry().getToolBarId()).add(
                unresolvedToolBarEntry);
    }

    public boolean containsUnresolvedToolBarEntries(String toolBarId) {
        return unresolvedToolBarEntryDescriptors.containsKey(toolBarId);
    }

    public List<UnresolvedEntry<D>> removeUnresolvedToolBarEntries(String toolBarId) {
        return unresolvedToolBarEntryDescriptors.remove(toolBarId);
    }

    public List<UnresolvedEntry<D>> removeUnresolvedToolBarEntries() {
        List<UnresolvedEntry<D>> unresolvedEntries = new ArrayList<>();
        for (Map.Entry<String, List<UnresolvedEntry<D>>> entry : unresolvedToolBarEntryDescriptors.entrySet()) {
            unresolvedEntries.addAll(entry.getValue());
        }
        unresolvedToolBarEntryDescriptors.clear();
        return unresolvedEntries;
    }
}
