/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.lib.util.context;

import java.util.Collection;

/**
 *
 * @author puce
 */
public interface Context {

    <T> T find(Class<T> type);

    //TODO: retunr List? 
    //TODO: return <T> instead of <? extends T>?
    <T> Collection<? extends T> findAll(Class<T> type);

    void addContextListener(Class<?> type, ContextListener listener);

    void removeContextListener(Class<?> type, ContextListener listener);
}
