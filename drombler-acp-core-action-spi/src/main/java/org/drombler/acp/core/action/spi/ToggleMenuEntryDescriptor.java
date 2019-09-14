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
package org.drombler.acp.core.action.spi;

import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.action.MenuItemSupplierFactory;
import org.drombler.acp.core.action.PositionableMenuItemAdapterFactory;
import org.drombler.acp.core.action.jaxb.ToggleMenuEntryType;

/**
 * A toggle menu entry desciptor.
 *
 * @author puce
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <ToggleMenuItem> the GUI toolkit specific type for toggle menu items
 * @param <F> the sorting strategy specific menu item supplier factory type
 */
public class ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem extends MenuItem, F extends MenuItemSupplierFactory<MenuItem, F>> extends MenuEntryDescriptor<MenuItem, F> {

    private final String toggleGroupId;

    /**
     * Creates a new instance of this class.
     *
     * @param actionId the id of the toggle action for this toggle menu entry
     * @param toggleGroupId the toggle group id or null if the toggle menu entry should not be part of a toggle group
     * @param path the path
     * @param menuItemSupplierFactory the menu item supplier factory
     */
    public ToggleMenuEntryDescriptor(String actionId, String toggleGroupId, String path, F menuItemSupplierFactory) {
        super(actionId, path, menuItemSupplierFactory);
        this.toggleGroupId = toggleGroupId;
    }

    /**
     * Creates a new instance of this class. The toggle group id is set to null, so the toggle menu entry won't be part of a toggle group
     *
     * @param actionId he id of the toggle action for this toggle menu entry
     * @param path the path
     * @param menuItemSupplierFactory the menu item supplier factory
     */
    public ToggleMenuEntryDescriptor(String actionId, String path, F menuItemSupplierFactory) {
        this(actionId, null, path, menuItemSupplierFactory);
    }

    /**
     * Gets the toggle group id or null if the toggle menu entry should not be part of a toggle group.
     *
     * @return the toggle group id or null if the toggle menu entry should not be part of a toggle group
     */
    public String getToggleGroupId() {
        return toggleGroupId;
    }

    /**
     * Creates an instance of a {@link ToggleMenuEntryDescriptor} from a {@link ToggleMenuEntryType} unmarshalled from the application.xml.
     *
     * @param <MenuItem> the GUI toolkit specific type for menu items
     * @param <ToggleMenuItem> the GUI toolkit specific type for toggle menu items
     * @param menuEntryType the unmarshalled MenuEntryType
     * @return a ToggleMenuEntryDescriptor
     */
    public static <MenuItem, ToggleMenuItem extends MenuItem> ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem, PositionableMenuItemAdapterFactory<MenuItem>> createToggleMenuEntryDescriptor(
            ToggleMenuEntryType menuEntryType) {
        return new ToggleMenuEntryDescriptor<>(StringUtils.stripToNull(menuEntryType.getActionId()),
                StringUtils.stripToNull(menuEntryType.getToggleGroupId()),
                StringUtils.stripToEmpty(menuEntryType.getPath()),
                new PositionableMenuItemAdapterFactory<>(menuEntryType.getPosition()));
    }
}
