/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author puce
 */
public class ActionResolutionManager<T> {

    private final Map<String, List<UnresolvedEntry<T>>> unresolvedToolBarEntryDescriptors = new HashMap<>();

    public void addUnresolvedEntry(String actionId, UnresolvedEntry<T> unresolvedToolBarEntry) {
        if (!unresolvedToolBarEntryDescriptors.containsKey(actionId)) {
            unresolvedToolBarEntryDescriptors.put(actionId, new ArrayList<UnresolvedEntry<T>>());
        }
        unresolvedToolBarEntryDescriptors.get(actionId).add(unresolvedToolBarEntry);
    }

    public boolean containsUnresolvedEntries(String actionId) {
        return unresolvedToolBarEntryDescriptors.containsKey(actionId);
    }

    public List<UnresolvedEntry<T>> removeUnresolvedEntries(String actionId) {
        return unresolvedToolBarEntryDescriptors.remove(actionId);
    }
}
