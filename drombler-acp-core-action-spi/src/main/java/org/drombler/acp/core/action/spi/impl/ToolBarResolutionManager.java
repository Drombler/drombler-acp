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
import java.util.List;
import org.drombler.acp.core.action.jaxb.ToolBarType;
import org.drombler.acp.core.action.spi.ToolBarDescriptor;
import org.drombler.acp.core.commons.util.UnresolvedEntry;

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
