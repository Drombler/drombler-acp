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
import org.drombler.acp.core.action.jaxb.MenuEntryType;

/**
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <F> the sorting strategy specific menu item supplier factory type
 * @author puce
 */
public class MenuEntryDescriptor<MenuItem, F extends MenuItemSupplierFactory<MenuItem>> extends AbstractMenuEntryDescriptor<MenuItem, F> {

    private final String actionId;

    public MenuEntryDescriptor(String actionId, String path, F menuItemSupplierFactory) {
        super(path, menuItemSupplierFactory);
        this.actionId = actionId;
    }

//    public ActionDescriptor getAction() {
//        return action;
//    }
    public String getActionId() {
        return actionId;
    }

//StringUtils.stripToNull(menuEntryType.getId()),StringUtils.stripToEmpty(menuEntryType.getPath()), menuEntryType.getPosition()
    public static <MenuItem> MenuEntryDescriptor<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> createMenuEntryDescriptor(MenuEntryType menuEntryType) {
        return new MenuEntryDescriptor<>(StringUtils.stripToNull(menuEntryType.getActionId()),
                StringUtils.stripToEmpty(menuEntryType.getPath()), new PositionableMenuItemAdapterFactory<>(menuEntryType.getPosition(), false));
    }
}
