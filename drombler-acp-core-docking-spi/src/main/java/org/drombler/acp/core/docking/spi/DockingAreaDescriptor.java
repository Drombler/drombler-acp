/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2012 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.docking.spi;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.drombler.acp.core.docking.jaxb.DockingAreaType;

/**
 *
 * @author puce
 */
public class DockingAreaDescriptor {

    private String id;
    private int position;
    private List<Integer> path;
    private boolean permanent;
    private LayoutConstraintsDescriptor layoutConstraints;

    public static DockingAreaDescriptor createDockingAreaDescriptor(DockingAreaType dockingArea) {
        DockingAreaDescriptor dockingAreaDescriptor = new DockingAreaDescriptor();
        dockingAreaDescriptor.setId(StringUtils.stripToNull(dockingArea.getId()));
        dockingAreaDescriptor.setPosition(dockingArea.getPosition());
        dockingAreaDescriptor.setPath(new ArrayList<>(dockingArea.getPaths().getPath()));
        dockingAreaDescriptor.setPermanent(dockingArea.isPermanent());
        dockingAreaDescriptor.setLayoutConstraints(LayoutConstraintsDescriptor.createLayoutConstraintsDescriptor(
                dockingArea.getLayoutConstraints()));
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

    /**
     * @return the permanent
     */
    public boolean isPermanent() {
        return permanent;
    }

    /**
     * @param permanent the permanent to set
     */
    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    /**
     * @return the layoutConstraints
     */
    public LayoutConstraintsDescriptor getLayoutConstraints() {
        return layoutConstraints;
    }

    /**
     * @param layoutConstraints the layoutConstraints to set
     */
    public void setLayoutConstraints(LayoutConstraintsDescriptor layoutConstraints) {
        this.layoutConstraints = layoutConstraints;
    }
}