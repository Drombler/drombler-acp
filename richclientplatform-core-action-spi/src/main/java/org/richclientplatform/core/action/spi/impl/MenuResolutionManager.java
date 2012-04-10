/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.richclientplatform.core.action.spi.MenuDescriptor;


/**
 *
 * @author puce
 */
public class MenuResolutionManager {

    private final Map<String, MenuResolutionManager> unresolvedMenuItemContainers = new HashMap<>();
    private final Map<String, List<MenuDescriptor>> unresolvedMenuDescriptors = new HashMap<>();
    private final Map<String, List<UnresolvedMenuEntry>> unresolvedMenuEntryDescriptors = new HashMap<>();

    public void addUnresolvedMenu(String pathId, MenuDescriptor menuDescriptor) {
        if (!unresolvedMenuDescriptors.containsKey(pathId)) {
            unresolvedMenuDescriptors.put(pathId, new ArrayList<MenuDescriptor>());
        }
        unresolvedMenuDescriptors.get(pathId).add(menuDescriptor);
    }

    public boolean containsUnresolvedMenus(String pathId) {
        return unresolvedMenuDescriptors.containsKey(pathId);
    }

    public List<MenuDescriptor> removeUnresolvedMenus(String pathId) {
        return unresolvedMenuDescriptors.remove(pathId);
    }

    public MenuResolutionManager getMenuResolutionManager(String pathId) {
        if (!unresolvedMenuItemContainers.containsKey(pathId)) {
            unresolvedMenuItemContainers.put(pathId, new MenuResolutionManager());
        }
        return unresolvedMenuItemContainers.get(pathId);
    }

    public void addUnresolvedMenuEntry(String pathId, UnresolvedMenuEntry unresolvedMenuEntry) {
        if (!unresolvedMenuEntryDescriptors.containsKey(pathId)) {
            unresolvedMenuEntryDescriptors.put(pathId, new ArrayList<UnresolvedMenuEntry>());
        }
        unresolvedMenuEntryDescriptors.get(pathId).add(unresolvedMenuEntry);
    }

    public List<UnresolvedMenuEntry> removeUnresolvedMenuEntries(String pathId) {
        return unresolvedMenuEntryDescriptors.remove(pathId);
    }

    public boolean containsUnresolvedMenuEntries(String pathId) {
        return unresolvedMenuEntryDescriptors.containsKey(pathId);
    }
}
