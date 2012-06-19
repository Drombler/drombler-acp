/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.lib.util;

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
