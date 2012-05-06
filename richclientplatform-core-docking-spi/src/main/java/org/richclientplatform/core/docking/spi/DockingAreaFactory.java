/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.spi;

/**
 *
 * @author puce
 */
public interface DockingAreaFactory<A> {

    A createDockingArea(DockingAreaDescriptor dockingAreaDescriptor);
}
