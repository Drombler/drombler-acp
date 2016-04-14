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

import org.softsmithy.lib.util.ResourceLoader;

/**
 *
 * @author puce
 */
public class AbstractDockableDockingDescriptor<D> {

    private final Class<D> dockableClass;
    private final ResourceLoader resourceLoader;
    private String id;
    private String areaId;
    private String icon;

    public AbstractDockableDockingDescriptor(Class<D> dockableClass) {
        this.dockableClass = dockableClass;
        this.resourceLoader = new ResourceLoader(dockableClass);
    }

    /**
     * @return the areaId
     */
    public String getAreaId() {
        return areaId;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param areaId the areaId to set
     */
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the dockableClass
     */
    public Class<D> getDockableClass() {
        return dockableClass;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

}
