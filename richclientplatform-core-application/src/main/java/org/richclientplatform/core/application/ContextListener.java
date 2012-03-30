/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.application;

import java.util.EventListener;

/**
 *
 * @author puce
 */
public interface ContextListener<T> extends EventListener{

    void addingToContext(T content);

    //TODO: needed?
//    void modifiedExtension(Bundle bundle, BundleEvent event, T extension);
    void removedFromContext(T content);
}
