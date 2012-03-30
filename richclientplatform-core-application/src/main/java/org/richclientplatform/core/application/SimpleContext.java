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
public class SimpleContext implements Context{

    @Override
    public <T> T find(Class<T> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> List<? extends T> findAll(Class<T> type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> void track(Class<T> type, ContextListener<T> listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
