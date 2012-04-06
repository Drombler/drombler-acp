/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.richclientplatform.core.docking.Dockable;
import org.richclientplatform.core.docking.jaxb.DockingType;
import org.richclientplatform.core.util.Resources;

/**
 *
 * @author puce
 */
public class DockingDescriptor {

    private String displayName;
    private String icon;
    private Dockable dockable;

    public static DockingDescriptor createDockingDescriptor(DockingType docking, Bundle bundle) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        DockingDescriptor dockingDescriptor = new DockingDescriptor();
        @SuppressWarnings("unchecked")
        Class<? extends Dockable> dockableClass = (Class<? extends Dockable>) bundle.loadClass(StringUtils.stripToNull(docking.getDockableClass()));
        dockingDescriptor.setDisplayName(Resources.getResourceString(dockableClass, docking.getDisplayName()));
        dockingDescriptor.setIcon(StringUtils.stripToNull(docking.getIcon()));
        dockingDescriptor.setDockable(dockableClass.newInstance());
        return dockingDescriptor;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return the dockable
     */
    public Dockable getDockable() {
        return dockable;
    }

    /**
     * @param dockable the dockable to set
     */
    public void setDockable(Dockable dockable) {
        this.dockable = dockable;
    }
}
