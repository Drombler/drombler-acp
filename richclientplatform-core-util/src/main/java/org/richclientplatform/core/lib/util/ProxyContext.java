/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.lib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author puce
 */
public class ProxyContext extends AbstractContext {

    private final List<Context> contexts = new ArrayList<>();

    @Override
    public <T> T find(Class<T> type) {
        for (Context context : contexts) {
            T result = context.find(type);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public <T> Collection<? extends T> findAll(Class<T> type) {
        List<T> result = new ArrayList<>();

        for (Context context : contexts) {
            result.addAll(context.findAll(type));
        }

        return result;
    }

    @Override
    public void addContextListener(Class<?> type, ContextListener listener) {
        super.addContextListener(type, listener);

        for (Context context : contexts) {
            context.addContextListener(type, listener);
        }
    }

    @Override
    public void removeContextListener(Class<?> type, ContextListener listener) {
        super.removeContextListener(type, listener);
        for (Context context : contexts) {
            context.removeContextListener(type, listener);
        }
    }

//    @Override
//    public <T> void track(Class<T> type, ContextListener<T> listener) {
//        if (contexts != null) {
//            for (Context context : contexts) {
//                context.track(type, listener);
//            }
//        }
//    }
    public void addContext(Context context) {
        contexts.add(context);
        for (Map.Entry<Class<?>, List<ContextListener>> entry : getListeners().entrySet()) {
            for (ContextListener contextListener : entry.getValue()) {
                context.addContextListener(entry.getKey(), contextListener);
            }
            if (!context.findAll(entry.getKey()).isEmpty()) {
                fireContextEvent(entry.getKey());
            }
        }
    }

    public void removeContext(Context context) {
        contexts.remove(context);
        for (Map.Entry<Class<?>, List<ContextListener>> entry : getListeners().entrySet()) {
            for (ContextListener contextListener : entry.getValue()) {
                context.removeContextListener(entry.getKey(), contextListener);
            }
            if (!context.findAll(entry.getKey()).isEmpty()) {
                fireContextEvent(entry.getKey());
            }
        }
    }
}
