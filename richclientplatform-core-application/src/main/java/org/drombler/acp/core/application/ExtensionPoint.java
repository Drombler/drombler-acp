/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.application;

/**
 *
 * @author puce
 */
public interface ExtensionPoint<T> {

    Class<T> getJAXBRootClass();

//    Collection<T> getExtensions();
}
