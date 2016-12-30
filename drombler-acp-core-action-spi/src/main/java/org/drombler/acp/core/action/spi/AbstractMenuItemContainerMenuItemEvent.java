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

import org.drombler.acp.core.action.MenuItemSupplier;
import java.util.EventObject;
import java.util.List;

/**
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @param <M> the type of the menu item
 * @author puce
 */
public abstract class AbstractMenuItemContainerMenuItemEvent<MenuItem, Menu extends MenuItem, M extends MenuItem> extends EventObject {

    private final MenuItemSupplier<? extends M> menuItem;
    private final List<String> path;

    public AbstractMenuItemContainerMenuItemEvent(MenuItemContainer<MenuItem, Menu, ?> source, MenuItemSupplier<? extends M> menuItem, List<String> path) {
        super(source);
        this.menuItem = menuItem;
        this.path = path;
    }

    /**
     * @return the path
     */
    public List<String> getPath() {
        return path;
    }

    /**
     * @return the menuItem
     */
    public MenuItemSupplier<? extends M> getMenuItem() {
        return menuItem;
    }
}
