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
package org.drombler.acp.core.action.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.drombler.acp.core.commons.util.UnresolvedEntry;

/**
 * An action resolution manager which manages entries, which depend on actions which have not yet been registered.
 *
 * @author puce
 */
public class ActionResolutionManager<T> {

    private final Map<String, List<UnresolvedEntry<T>>> unresolvedEntries = new HashMap<>();

    /**
     * Adds an unresolved entry.
     *
     * @param actionId the action id of the action the entry depends on
     * @param unresolvedEntry the unresolved entry
     */
    public void addUnresolvedEntry(String actionId, UnresolvedEntry<T> unresolvedEntry) {
        if (!unresolvedEntries.containsKey(actionId)) {
            unresolvedEntries.put(actionId, new ArrayList<>());
        }
        unresolvedEntries.get(actionId).add(unresolvedEntry);
    }

    /**
     * Checks if this manager contains any unresolved entries for the specified action id.
     *
     * @param actionId the action id
     * @return true, if this manager contains any unresolved entries for the specified action id, else false
     */
    public boolean containsUnresolvedEntries(String actionId) {
        return unresolvedEntries.containsKey(actionId);
    }

    /**
     * Gets and removes all unresolved entries associated with the provided action id.
     *
     * @param actionId the action id
     * @return the unresolved entries associated with the provided action id
     */
    public List<UnresolvedEntry<T>> removeUnresolvedEntries(String actionId) {
        return unresolvedEntries.remove(actionId);
    }
}
