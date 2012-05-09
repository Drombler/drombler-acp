/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.spi;

/**
 *
 * @author puce
 */
public interface DockableFactory<T> {

    T createDockable(ViewDockingDescriptor dockingDescriptor);
}
