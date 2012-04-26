/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.richclientplatform.core.action.jaxb.ToolBarType;
import org.richclientplatform.core.action.spi.ToolBarDescriptor;
import org.richclientplatform.core.action.spi.ToolBarEntryDescriptor;

/**
 *
 * @author puce
 */
public class ToolBarEntryResolutionManager<D extends ToolBarEntryDescriptor> {

    private final Map<String, List<UnresolvedEntry<D>>> unresolvedToolBarEntryDescriptors = new HashMap<>();

    public void addUnresolvedToolBarEntry(UnresolvedEntry<D> unresolvedToolBarEntry) {
        if (!unresolvedToolBarEntryDescriptors.containsKey(unresolvedToolBarEntry.getEntry().getToolBarId())) {
            unresolvedToolBarEntryDescriptors.put(unresolvedToolBarEntry.getEntry().getToolBarId(),
                    new ArrayList<UnresolvedEntry<D>>());
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
