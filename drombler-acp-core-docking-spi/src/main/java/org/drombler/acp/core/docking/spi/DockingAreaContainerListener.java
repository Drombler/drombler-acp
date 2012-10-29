/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking.spi;

import java.util.EventListener;

/**
 * TODO: more methods might be added in future
 *
 * @author puce
 */
public interface DockingAreaContainerListener<A, D> extends EventListener {

    void dockingAreaAdded(DockingAreaContainerDockingAreaEvent<A, D> event);
}
