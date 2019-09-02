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

import java.util.List;
import org.drombler.acp.core.action.MenuItemSupplier;

/**
 * A {@link MenuItemContainer} menu item event.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @author puce
 */
public class MenuItemContainerMenuItemEvent<MenuItem, Menu extends MenuItem> extends AbstractMenuItemContainerMenuItemEvent<MenuItem, Menu, MenuItem> {

    private static final long serialVersionUID = 1572045115559027950L;

    /**
     * Creates a new instance of this class.
     *
     * @param source the source container of this event
     * @param menuItemSupplier the menu item supplier
     * @param path the path of the menu item
     */
    public MenuItemContainerMenuItemEvent(MenuItemContainer<MenuItem, Menu, ?> source, MenuItemSupplier<? extends MenuItem> menuItemSupplier, List<String> path) {
        super(source, menuItemSupplier, path);
    }
}
