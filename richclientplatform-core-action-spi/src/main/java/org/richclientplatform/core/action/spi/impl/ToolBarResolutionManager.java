/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.ArrayList;
import java.util.List;
import org.richclientplatform.core.action.jaxb.ToolBarType;
import org.richclientplatform.core.action.spi.ToolBarDescriptor;

/**
 *
 * @author puce
 */
public class ToolBarResolutionManager {

    private final List<UnresolvedEntry<ToolBarDescriptor>> unresolvedToolBarDescriptors = new ArrayList<>();
    private final List<UnresolvedEntry<ToolBarType>> unresolvedToolBarTypes = new ArrayList<>();

    // TODO: better name?
    public void addUnresolvedToolBarType(UnresolvedEntry<ToolBarType> unresolvedEntry) {
        unresolvedToolBarTypes.add(unresolvedEntry);
    }

    // TODO: better name?
    public List<UnresolvedEntry<ToolBarType>> removeUnresolvedToolBarTypes() {
        List<UnresolvedEntry<ToolBarType>> toolBarTypes = new ArrayList<>(unresolvedToolBarTypes);
        unresolvedToolBarTypes.clear();
        return toolBarTypes;
    }

    public void addUnresolvedToolBar(UnresolvedEntry<ToolBarDescriptor> toolBarDescriptor) {
        unresolvedToolBarDescriptors.add(toolBarDescriptor);
    }

    public List<UnresolvedEntry<ToolBarDescriptor>> removeUnresolvedToolBars() {
        List<UnresolvedEntry<ToolBarDescriptor>> toolBarDescriptors = new ArrayList<>(unresolvedToolBarDescriptors);
        unresolvedToolBarDescriptors.clear();
        return toolBarDescriptors;
    }
}
