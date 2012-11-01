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
package org.drombler.acp.core.commons.util.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author puce
 */
public abstract class AbstractContext implements Context {

    private final Map<Class<?>, List<ContextListener>> listeners = new HashMap<>();

    @Override
    public void addContextListener(Class<?> type, ContextListener listener) {
        if (!listeners.containsKey(type)) {
            getListeners().put(type, new ArrayList<ContextListener>());
        }
        getListeners().get(type).add(listener);

    }

    @Override
    public void removeContextListener(Class<?> type, ContextListener listener) {
        if (getListeners().containsKey(type)) {
            getListeners().get(type).remove(listener);
        }
    }

    protected void fireContextEvent(Class<?> type) {
        if (getListeners().containsKey(type)) {
            ContextEvent event = new ContextEvent(this);
            for (ContextListener listener : getListeners().get(type)) {
                listener.contextChanged(event);
            }
        }
    }

    protected Map<Class<?>, List<ContextListener>> getListeners() {
        return listeners;
    }
}