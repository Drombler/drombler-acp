/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing;

import java.util.List;
import org.richclientplatform.core.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
public interface DockingAreaContainer<A, D> {

    void addDockingArea(List<Integer> path, A dockingArea);

    void addDockable(String areaId, PositionableAdapter<D> dockable);

    public void addDockingAreaContainerListener(DockingAreaContainerListener<A, D> listener);

    public void removeDockingAreaContainerListener(DockingAreaContainerListener<A, D> listener);
}
