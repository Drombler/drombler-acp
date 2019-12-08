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
 * A {@link MenuItemContainer} menu event.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @author puce
 */
public class MenuItemContainerMenuEvent<MenuItem, Menu extends MenuItem> extends AbstractMenuItemContainerMenuItemEvent<MenuItem, Menu, Menu> {

    private static final long serialVersionUID = 6108086512276402172L;

    private final String menuId;

    /**
     * Creates a new instance of this class.
     *
     * @param source the source of this event
     * @param menuSupplier the menu supplier
     * @param menuId the id of the menu
     * @param path the path of the menu
     */
    public MenuItemContainerMenuEvent(MenuItemContainer<MenuItem, Menu, ?> source, MenuItemSupplier<? extends Menu> menuSupplier, String menuId, List<String> path) {
        super(source, menuSupplier, path);
        this.menuId = menuId;
    }

    /**
     * Gets the id of the menu.
     *
     * @return the id of the menu
     */
    public String getMenuId() {
        return menuId;
    }
}
