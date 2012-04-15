/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.richclientplatform.core.docking.jaxb.DockingAreaType;

/**
 *
 * @author puce
 */
public class DockingAreaDescriptor {

    private String id;
    private int position;
    private List<Integer> path;

    public static DockingAreaDescriptor createDockingAreaDescriptor(DockingAreaType dockingArea) {
        DockingAreaDescriptor dockingAreaDescriptor = new DockingAreaDescriptor();
        dockingAreaDescriptor.setId(StringUtils.stripToNull(dockingArea.getId()));
        dockingAreaDescriptor.setPosition(dockingArea.getPosition());
        dockingAreaDescriptor.setPath(new ArrayList<>(dockingArea.getPaths().getPath()));
        return dockingAreaDescriptor;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the pathDescriptors
     */
    public List<Integer> getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(List<Integer> path) {
        this.path = path;
    }
}
