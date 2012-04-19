/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.richclientplatform.core.action.spi.AbstractMenuEntryDescriptor;


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
