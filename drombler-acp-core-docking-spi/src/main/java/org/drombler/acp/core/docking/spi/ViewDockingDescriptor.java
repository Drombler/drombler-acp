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
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.MenuEntryDescriptor;
import org.drombler.acp.core.docking.jaxb.ViewDockingType;
import org.drombler.acp.core.docking.spi.impl.ActivateDockableAction;
import org.drombler.commons.client.util.MnemonicUtils;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 * @param <D>
 */
public class ViewDockingDescriptor<D> extends AbstractDockableDockingDescriptor<D> {

    private String displayName;
    private int position;
    private ActionDescriptor<ActivateDockableAction<D>> activateDockableActionDescriptor;
    private MenuEntryDescriptor activateDockableMenuEntryDescriptor;
    private ResourceBundle resourceBundle;

    public ViewDockingDescriptor(Class<D> dockableClass) {
        super(dockableClass);
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
        this.activateDockableActionDescriptor.setListener(new ActivateDockableAction<>(dockable));
    }

    public static ViewDockingDescriptor<?> createViewDockingDescriptor(ViewDockingType docking, Bundle bundle) throws
            ClassNotFoundException, InstantiationException, IllegalAccessException {
        final Class<?> dockableClass = bundle.loadClass(StringUtils.stripToNull(docking.getDockableClass()));
        return createViewDockingDescriptor(docking, dockableClass, bundle);
    }

    private static <D> ViewDockingDescriptor<D> createViewDockingDescriptor(ViewDockingType docking,
            Class<D> dockableClass, Bundle bundle) throws ClassNotFoundException {
        ViewDockingDescriptor<D> dockingDescriptor = new ViewDockingDescriptor<>(dockableClass);

        DockingDescriptorUtils.configureDockingDescriptor(dockingDescriptor, docking, bundle);
        dockingDescriptor.setResourceBundle(ResourceBundleUtils.getResourceBundle(dockingDescriptor.getDockableClass(),
                docking.getResourceBundleBaseName(), docking.getDisplayName()));
        String displayName = ResourceBundleUtils.getResourceStringPrefixed(docking.getDisplayName(), dockingDescriptor.
                getResourceBundle());
        dockingDescriptor.setDisplayName(MnemonicUtils.removeMnemonicChar(displayName));
        dockingDescriptor.setPosition(docking.getPosition());
        dockingDescriptor.activateDockableActionDescriptor = createActivateDockableActionDescriptor(dockingDescriptor,
                displayName, docking.getAccelerator());
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

    private static <D> ActionDescriptor<ActivateDockableAction<D>> createActivateDockableActionDescriptor(
            ViewDockingDescriptor<D> dockingDescriptor, String displayName, String accelerator) {
        ActionDescriptor<ActivateDockableAction<D>> actionDescriptor
                = new ActionDescriptor<>((Class<ActivateDockableAction<D>>) (Class<?>) ActivateDockableAction.class,
                        dockingDescriptor.getResourceLoader());
        actionDescriptor.setId(dockingDescriptor.getId());
        actionDescriptor.setDisplayName(displayName);
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

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

}
