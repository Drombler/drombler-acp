/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.richclientplatform.core.action.spi.ToolBarDescriptor;
import org.richclientplatform.core.action.spi.ToolBarEntryDescriptor;

/**
 *
 * @author puce
 */
public class ToolBarResolutionManager {

    private final List<UnresolvedEntry<ToolBarDescriptor>> unresolvedToolBarDescriptors = new ArrayList<>();
    private final Map<String, List<UnresolvedEntry<ToolBarEntryDescriptor>>> unresolvedToolBarEntryDescriptors = new HashMap<>();

    public void addUnresolvedToolBar(UnresolvedEntry<ToolBarDescriptor> toolBarDescriptor) {
        unresolvedToolBarDescriptors.add(toolBarDescriptor);
    }

    public List<UnresolvedEntry<ToolBarDescriptor>> removeUnresolvedToolBars() {
        List<UnresolvedEntry<ToolBarDescriptor>> toolBarDescriptors = new ArrayList<>(unresolvedToolBarDescriptors);
        unresolvedToolBarDescriptors.clear();
        return toolBarDescriptors;
    }

    public void addUnresolvedToolBarEntry(UnresolvedEntry<ToolBarEntryDescriptor> unresolvedToolBarEntry) {
        if (!unresolvedToolBarEntryDescriptors.containsKey(unresolvedToolBarEntry.getEntry().getToolBarId())) {
            unresolvedToolBarEntryDescriptors.put(unresolvedToolBarEntry.getEntry().getToolBarId(),
                    new ArrayList<UnresolvedEntry<ToolBarEntryDescriptor>>());
        }
        unresolvedToolBarEntryDescriptors.get(unresolvedToolBarEntry.getEntry().getToolBarId()).add(
                unresolvedToolBarEntry);
    }

    public boolean containsUnresolvedMenuEntries(String toolBarId) {
        return unresolvedToolBarEntryDescriptors.containsKey(toolBarId);
    }

    public List<UnresolvedEntry<ToolBarEntryDescriptor>> removeUnresolvedToolBarEntries(String toolBarId) {
        return unresolvedToolBarEntryDescriptors.remove(toolBarId);
    }
}
