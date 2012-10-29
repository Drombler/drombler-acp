/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking.spi;

import java.util.List;

/**
 *
 * @author puce
 */
public interface DockingAreaContainer<A, D> {

    void addDockingArea(List<Integer> path, A dockingArea);

    void addDockable(D dockable);

    public void addDockingAreaContainerListener(DockingAreaContainerListener<A, D> listener);

    public void removeDockingAreaContainerListener(DockingAreaContainerListener<A, D> listener);
}
