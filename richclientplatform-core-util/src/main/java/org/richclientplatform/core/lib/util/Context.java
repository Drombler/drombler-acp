/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.lib.util;

import java.util.Collection;

/**
 *
 * @author puce
 */
public interface Context {

    <T> T find(Class<T> type);

    <T> Collection<? extends T> findAll(Class<T> type);

    void addContextListener(Class<?> type, ContextListener listener);

    void removeContextListener(Class<?> type, ContextListener listener);
}
