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

import java.util.ResourceBundle;
import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.MenuEntryDescriptor;
import org.drombler.acp.core.docking.spi.impl.ActivateViewAction;
import org.drombler.commons.client.util.MnemonicUtils;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.softsmithy.lib.util.ResourceLoader;

/**
 *
 * @author puce
 * @param <D>
 */
public class ViewDockingDescriptor<D> extends AbstractDockableDockingDescriptor<D> {

    private final String displayName;
    private int position;
    private final ActionDescriptor<ActivateViewAction<D>> activateDockableActionDescriptor;
    private MenuEntryDescriptor activateDockableMenuEntryDescriptor;
    private final ResourceBundle resourceBundle;
    private final ResourceLoader resourceLoader;
    private String areaId;
    private final String icon;

    /**
     *
     * @param dockableClass
     * @param id
     * @param displayName
     * @param accelerator
     * @param icon
     * @param resourceBundleBaseName
     */
    public ViewDockingDescriptor(Class<D> dockableClass, String id, String displayName, String icon, String accelerator, String resourceBundleBaseName) {
        super(dockableClass, id);
        this.resourceLoader = new ResourceLoader(dockableClass);
        this.resourceBundle = ResourceBundleUtils.getResourceBundle(dockableClass, resourceBundleBaseName, displayName);
        String localizedDisplayName = ResourceBundleUtils.getResourceStringPrefixed(displayName, resourceBundle);

        this.displayName = MnemonicUtils.removeMnemonicChar(localizedDisplayName);
        this.icon = icon;
        this.activateDockableActionDescriptor = createActivateDockableActionDescriptor(id, localizedDisplayName, accelerator, icon);
    }

    private ActionDescriptor<ActivateViewAction<D>> createActivateDockableActionDescriptor(String id, String displayName, String accelerator, String icon) {
        ActionDescriptor<ActivateViewAction<D>> actionDescriptor
                = new ActionDescriptor<>((Class<ActivateViewAction<D>>) (Class<?>) ActivateViewAction.class, resourceLoader);
        actionDescriptor.setId(id);
        actionDescriptor.setDisplayName(displayName);
        actionDescriptor.setAccelerator(accelerator);
        actionDescriptor.setIcon(icon);
//        actionDescriptor.setListener(new ActivateViewAction(dockingDescriptor.getDockable()));
        return actionDescriptor;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @return the areaId
     */
    public String getAreaId() {
        return areaId;
    }

    /**
     * @param areaId the areaId to set
     */
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
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
    public ActionDescriptor<?> getActivateDockableActionDescriptor() {
        return activateDockableActionDescriptor;
    }

    public void setDockable(D dockable) {
        this.activateDockableActionDescriptor.setListener(new ActivateViewAction<>(dockable));
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

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

}
