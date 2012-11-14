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

import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.MenuEntryDescriptor;
import org.drombler.acp.core.commons.util.Resources;
import org.drombler.acp.core.docking.Dockable;
import org.drombler.acp.core.docking.jaxb.ViewDockingType;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
public class ViewDockingDescriptor extends AbstractDockableDockingDescriptor {

    private String displayName;
    private int position;
    private ActionDescriptor activateDockableActionDescriptor;
    private MenuEntryDescriptor activateDockableMenuEntryDescriptor;

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

    public static ViewDockingDescriptor createViewDockingDescriptor(ViewDockingType docking, Bundle bundle) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ViewDockingDescriptor dockingDescriptor = new ViewDockingDescriptor();
        dockingDescriptor.setId(StringUtils.stripToNull(docking.getId()));
        @SuppressWarnings("unchecked")
        Class<? extends Dockable> dockableClass = (Class<? extends Dockable>) bundle.loadClass(StringUtils.stripToNull(
                docking.getDockableClass()));
        dockingDescriptor.setDisplayName(Resources.getResourceString(dockableClass, docking.getDisplayName()));
        dockingDescriptor.setIcon(StringUtils.stripToNull(docking.getIcon()));
        dockingDescriptor.setAreaId(StringUtils.stripToNull(docking.getAreaId()));
        dockingDescriptor.setPosition(docking.getPosition());
        dockingDescriptor.setDockableClass(bundle.loadClass(StringUtils.stripToNull(docking.getDockableClass())));
        dockingDescriptor.setActivateDockableActionDescriptor(createActivateDockableActionDescriptor(dockingDescriptor,
                docking.getAccelerator()));
        dockingDescriptor.setActivateDockableMenuEntryDescriptor(new MenuEntryDescriptor(dockingDescriptor.getId(),
                getWindowPath(docking), docking.getMenuEntry().getPosition()));
        return dockingDescriptor;
    }

    private static String getWindowPath(ViewDockingType docking) {
        StringBuilder sb = new StringBuilder("Window");
        if (docking.getMenuEntry().getPath() != null) {
            sb.append("/");
            sb.append(docking.getMenuEntry().getPath());
        }
        return sb.toString();
    }

    private static ActionDescriptor createActivateDockableActionDescriptor(ViewDockingDescriptor dockingDescriptor, String accelerator) {
        ActionDescriptor actionDescriptor = new ActionDescriptor();
        actionDescriptor.setId(dockingDescriptor.getId());
        actionDescriptor.setDisplayName(dockingDescriptor.getDisplayName());
        actionDescriptor.setAccelerator(accelerator);
        actionDescriptor.setIcon(dockingDescriptor.getIcon());
//        actionDescriptor.setListener(new ActivateDockableAction(dockingDescriptor.getDockable()));
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
