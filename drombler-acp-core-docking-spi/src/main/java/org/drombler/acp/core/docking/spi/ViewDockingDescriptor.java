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
import org.drombler.acp.core.docking.ViewDocking;
import org.drombler.acp.core.docking.spi.impl.ActivateViewAction;
import org.drombler.commons.client.util.MnemonicUtils;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.softsmithy.lib.util.ResourceLoader;

/**
 * A view docking descriptor.
 *
 * @author puce
 * @param <D> the Dockable type
 * @param <DATA> the Dockable data type
 * @param <E> the Dockable entry type
 */
public class ViewDockingDescriptor<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> extends AbstractDockableDockingDescriptor<D> {

    private final String displayName;
    private int position;
    private final ActionDescriptor<ActivateViewAction<D, DATA, E>> activateDockableActionDescriptor;
    private MenuEntryDescriptor activateDockableMenuEntryDescriptor;
    private final ResourceLoader resourceLoader;
    private String areaId;
    private final String icon;

    /**
     * Creates a new instance of this class.
     *
     * @param dockableClass the type of the Dockable
     * @param id the id of the Dockable
     * @param displayName the text to be displayed, e.g. as the text for tabs or menu items. If the value starts with '%' the rest of the value is interpreted as a property key and the value gets
     * looked-up in the resource bundle.
     * @param icon the icon name pattern to resolve the icons to be used for this view
     * @param accelerator the accelerator key combination to open this view. If the value starts with '%' the rest of the value is interpreted as a property key and the value gets looked-up in the
     * resource bundle.
     * @param resourceBundleBaseName the {@link ResourceBundle} base name following the same rules as in {@link ViewDocking#resourceBundleBaseName() }
     * @see ViewDocking
     */
    public ViewDockingDescriptor(Class<D> dockableClass, String id, String displayName, String icon, String accelerator, String resourceBundleBaseName) {
        super(dockableClass, id);
        this.resourceLoader = new ResourceLoader(dockableClass);
        this.icon = icon;

        boolean bundleRequired = ResourceBundleUtils.isPrefixedResourceString(displayName) || ResourceBundleUtils.isPrefixedResourceString(accelerator);
        ResourceBundle resourceBundle = bundleRequired
                ? ResourceBundleUtils.getConditionalResourceBundle(dockableClass, resourceBundleBaseName)
                : null;
        String localizedDisplayName = ResourceBundleUtils.getResourceStringPrefixed(displayName, resourceBundle);
        String localizedAccelerator = ResourceBundleUtils.getResourceStringPrefixed(accelerator, resourceBundle);

        this.displayName = MnemonicUtils.removeMnemonicChar(localizedDisplayName);
        this.activateDockableActionDescriptor = createActivateDockableActionDescriptor(id, localizedDisplayName, localizedAccelerator, icon);
    }

    private ActionDescriptor<ActivateViewAction<D, DATA, E>> createActivateDockableActionDescriptor(String id, String displayName, String accelerator, String icon) {
        ActionDescriptor<ActivateViewAction<D, DATA, E>> actionDescriptor
                = new ActionDescriptor<>((Class<ActivateViewAction<D, DATA, E>>) (Class<?>) ActivateViewAction.class, resourceLoader);
        actionDescriptor.setId(id);
        actionDescriptor.setDisplayName(displayName);
        actionDescriptor.setAccelerator(accelerator);
        actionDescriptor.setIcon(icon);
//        actionDescriptor.setListener(new ActivateViewAction(dockingDescriptor.getDockable()));
        return actionDescriptor;
    }

    /**
     * Gets the icon name pattern to resolve the icons to be used for this view.<br>
     * <br>
     * Note that this only specifies the name pattern. Drombler ACP looks for &lt;icon-base-name&gt;16.&lt;icon-extension&gt; for tabs and menu items (expected to be 16x16 pixels) and
     * &lt;icon-base-name&gt;24.&lt;icon-extension&gt; for toolbar buttons (expected to be 24x24 pixels). So if icon is &quot;test.png&quot;, Drombler ACP would look for test16.png (for tabs and menu
     * items) and test24.png (for toolbar buttons).
     *
     * @return the icon name pattern
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Gets the Docking Area ID to which this view should be docked.
     *
     * @return the Docking Area ID to which this view should be docked
     */
    public String getAreaId() {
        return areaId;
    }

    /**
     * Sets the Docking Area ID to which this view should be docked.
     *
     * @param areaId the Docking Area ID to which this view should be docked
     */
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * Gets the resource loader for loading resources such as icons.
     *
     * @return the resource loader
     */
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    /**
     * Gets the text to be displayed, e.g. as the text for tabs or menu items.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the position to order the views in a Docking Area.
     *
     * @return the position to order the views in a Docking Area
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position to order the views in a Docking Area.<br>
     * <br>
     * It's a best practice to leave out some positions between entries to allow other bundles to register entries between some existing ones.
     *
     * @param position the position to order the views in a Docking Area
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Gets the {@link ActionDescriptor} to activate the Dockable.
     *
     * @return the ActionDescriptor to activate the dockable.
     */
    public ActionDescriptor<?> getActivateDockableActionDescriptor() {
        return activateDockableActionDescriptor;
    }

    /**
     * Sets the view entry (a Dockable).
     *
     * @param viewEntry the view entry
     */
    public void setViewEntry(E viewEntry) {
        this.activateDockableActionDescriptor.setListener(new ActivateViewAction<>(viewEntry));
    }

    /**
     * Gets the {@link MenuEntryDescriptor} to activate the Dockable.
     *
     * @return the MenuEntryDescriptor to activate the Dockable
     */
    public MenuEntryDescriptor getActivateDockableMenuEntryDescriptor() {
        return activateDockableMenuEntryDescriptor;
    }

    /**
     * Sets the {@link MenuEntryDescriptor} to activate the Dockable.
     *
     * @param activateDockableMenuEntryDescriptor the MenuEntryDescriptor to activate the Dockable
     */
    public void setActivateDockableMenuEntryDescriptor(MenuEntryDescriptor activateDockableMenuEntryDescriptor) {
        this.activateDockableMenuEntryDescriptor = activateDockableMenuEntryDescriptor;
    }

}
