/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.lib.util.context;

import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author puce
 */
public class Contexts {

    private static Context EMPTY_CONTEXT;

    private Contexts() {
    }

    public static Context emptyContext() {
        if (EMPTY_CONTEXT == null) {
            EMPTY_CONTEXT = new Context() {

                @Override
                public <T> T find(Class<T> type) {
                    return null;
                }

                @Override
                public <T> Collection<? extends T> findAll(Class<T> type) {
                    return Collections.emptyList();
                }

                @Override
                public void addContextListener(Class<?> type, ContextListener listener) {
                    // there will be no changes -> nothing to do
                }

                @Override
                public void removeContextListener(Class<?> type, ContextListener listener) {
                    // there will be no changes -> nothing to do
                }
            };
        }
        return EMPTY_CONTEXT;
    }
}
