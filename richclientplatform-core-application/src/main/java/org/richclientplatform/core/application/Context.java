/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.application;

import java.util.List;

/**
 *
 * @author puce
 */
public interface Context {
    <T> T find(Class<T> type);
    <T> List<? extends T> findAll(Class<T> type);
    <T> void track(Class<T> type, ContextListener<T> listener);
}
