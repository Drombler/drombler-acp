/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing;

import java.util.EventObject;

/**
 *
 * @author puce
 */
public class DockingAreaContainerDockingAreaEvent<A, D> extends EventObject {
    private final String areaId;
    
//    private final List<ShortPathPart> shortPath;
//    private List<ShortPathPart> splitPah = null;
//    private Orientation splitOrientation;
    private final A dockingArea;

    public DockingAreaContainerDockingAreaEvent(DockingAreaContainer<A, D> source, String areaId, A dockingArea) {
        super(source);
        this.areaId = areaId;
        this.dockingArea = dockingArea;
    }

//    void addShortPath(ShortPathPart shortPathPart) {
//         shortPath.add(shortPathPart);
//    }
//
//    void split(Orientation orientation) {
//        if (isSplit()) {
//            throw new IllegalStateException("Only one split expected!");
//        }
//        splitPah = new ArrayList<>(getShortPath());
//        splitOrientation = orientation;
//    }
//
//    public boolean isSplit() {
//        return getSplitPah() != null;
//    }

    /**
     * @return the shortPath
     */
//    public List<ShortPathPart> getShortPath() {
//        return Collections.unmodifiableList(shortPath);
//    }

//    /**
//     * @return the splitPah
//     */
//    public List<ShortPathPart> getSplitPah() {
//        return Collections.unmodifiableList(splitPah);
//    }
//
//    /**
//     * @return the splitOrientation
//     */
//    public Orientation getSplitOrientation() {
//        return splitOrientation;
//    }

    public A getDockingArea() {
        return dockingArea;
    }

    /**
     * @return the areaId
     */
    public String getAreaId() {
        return areaId;
    }

}
