/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.richclientplatform.core.action.spi.ActionDescriptor;
import org.richclientplatform.core.action.spi.MenuDescriptor;
import org.richclientplatform.core.action.spi.MenuEntryDescriptor;
import org.richclientplatform.core.docking.Dockable;
import org.richclientplatform.core.docking.jaxb.DockingType;
import org.richclientplatform.core.docking.processing.impl.ActivateDockableAction;
import org.richclientplatform.core.lib.util.Resources;

/**
 *
 * @author puce
 */
public class DockingDescriptor {

    private String id;
    private String displayName;
    private String icon;
    private Dockable dockable;
    private ActionDescriptor activateDockableActionDescriptor;
    private MenuEntryDescriptor activateDockableMenuEntryDescriptor;

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

    /**
     * @return the activateDockableActionDescriptor
     */
    public ActionDescriptor getActivateDockableActionDescriptor() {
        return activateDockableActionDescriptor;
    }

    /**
     * @param activateDockableActionDescriptor the activateDockableActionDescriptor to set
     */
    public void setActivateDockableActionDescriptor(ActionDescriptor activateDockableActionDescriptor) {
        this.activateDockableActionDescriptor = activateDockableActionDescriptor;
    }

    public static DockingDescriptor createDockingDescriptor(DockingType docking, Bundle bundle) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        DockingDescriptor dockingDescriptor = new DockingDescriptor();
        dockingDescriptor.setId(docking.getId());
        @SuppressWarnings("unchecked")
        Class<? extends Dockable> dockableClass = (Class<? extends Dockable>) bundle.loadClass(StringUtils.stripToNull(
                docking.getDockableClass()));
        dockingDescriptor.setDisplayName(Resources.getResourceString(dockableClass, docking.getDisplayName()));
        dockingDescriptor.setIcon(StringUtils.stripToNull(docking.getIcon()));
        dockingDescriptor.setDockable(dockableClass.newInstance());
        dockingDescriptor.setActivateDockableActionDescriptor(createActivateDockableActionDescriptor(dockingDescriptor,
                docking.getAccelerator()));
        dockingDescriptor.setActivateDockableMenuEntryDescriptor(new MenuEntryDescriptor(dockingDescriptor.getId(),
                getWindowPath(docking), docking.getMenuEntry().getPosition()));
        return dockingDescriptor;
    }

    private static String getWindowPath(DockingType docking) {
        StringBuilder sb = new StringBuilder("Window");
        if (docking.getMenuEntry().getPath() != null) {
            sb.append("/");
            sb.append(docking.getMenuEntry().getPath());
        }
        return sb.toString();
    }

    private static ActionDescriptor createActivateDockableActionDescriptor(DockingDescriptor dockingDescriptor, String accelerator) {
        ActionDescriptor actionDescriptor = new ActionDescriptor();
        actionDescriptor.setId(dockingDescriptor.getId());
        actionDescriptor.setDisplayName(dockingDescriptor.getDisplayName());
        actionDescriptor.setAccelerator(accelerator);
        actionDescriptor.setIcon(dockingDescriptor.getIcon());
        actionDescriptor.setListener(new ActivateDockableAction(dockingDescriptor.getDockable()));
        return actionDescriptor;
    }

    /**
     * @return the activateDockableMenuEntryDescriptor
     */
    public MenuEntryDescriptor getActivateDockableMenuEntryDescriptor() {
        return activateDockableMenuEntryDescriptor;
    }

    /**
     * @param activateDockableMenuEntryDescriptor the activateDockableMenuEntryDescriptor to set
     */
    public void setActivateDockableMenuEntryDescriptor(MenuEntryDescriptor activateDockableMenuEntryDescriptor) {
        this.activateDockableMenuEntryDescriptor = activateDockableMenuEntryDescriptor;
    }
}
