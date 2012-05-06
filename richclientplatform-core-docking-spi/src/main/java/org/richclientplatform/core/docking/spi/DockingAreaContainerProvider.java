/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.spi;

/**
 *
 * @author puce
 */
public interface DockingAreaContainerProvider<A, D> {

    DockingAreaContainer<A, D> getDockingAreaContainer();
}
