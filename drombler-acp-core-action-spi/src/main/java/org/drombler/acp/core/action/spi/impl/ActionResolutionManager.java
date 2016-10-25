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
import org.drombler.acp.core.commons.util.UnresolvedEntry;

/**
 * 
 * @author puce
 */
public class ActionResolutionManager<T> {

    private final Map<String, List<UnresolvedEntry<T>>> unresolvedToolBarEntryDescriptors = new HashMap<>();

    public void addUnresolvedEntry(String actionId, UnresolvedEntry<T> unresolvedToolBarEntry) {
        if (!unresolvedToolBarEntryDescriptors.containsKey(actionId)) {
            unresolvedToolBarEntryDescriptors.put(actionId, new ArrayList<>());
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
