/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.richclientplatform.core.docking.jaxb.DockingAreaPathType;
import org.richclientplatform.core.docking.jaxb.DockingAreaType;

/**
 *
 * @author puce
 */
public class DockingAreaDescriptor {

    private String id;
    private List<DockingAreaPathDescriptor> pathDescriptors;

    public static DockingAreaDescriptor createDockingAreaDescriptor(DockingAreaType dockingArea) {
        DockingAreaDescriptor dockingAreaDescriptor = new DockingAreaDescriptor();
        dockingAreaDescriptor.setId(StringUtils.stripToNull(dockingArea.getId()));
        List<DockingAreaPathDescriptor> pathDescriptors = new ArrayList<>(dockingArea.getPaths().getPath().size());
        for (DockingAreaPathType path : dockingArea.getPaths().getPath()) {
            pathDescriptors.add(DockingAreaPathDescriptor.createDockingAreaPathDescriptor(path));
        }
        dockingAreaDescriptor.setPathDescriptors(pathDescriptors);
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
     * @return the pathDescriptors
     */
    public List<DockingAreaPathDescriptor> getPathDescriptors() {
        return pathDescriptors;
    }

    /**
     * @param pathDescriptors the pathDescriptors to set
     */
    public void setPathDescriptors(List<DockingAreaPathDescriptor> pathDescriptors) {
        this.pathDescriptors = pathDescriptors;
    }
}
