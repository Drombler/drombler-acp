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

import java.util.EventObject;
import java.util.List;
import org.drombler.acp.core.action.MenuItemSupplier;

/**
 * A base class for {@link MenuItemContainer} menu item events.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @param <M> the type of the menu item
 * @author puce
 */
public abstract class AbstractMenuItemContainerMenuItemEvent<MenuItem, Menu extends MenuItem, M extends MenuItem> extends EventObject {

    private final MenuItemSupplier<? extends M> menuItemSupplier;
    private final List<String> path;

    /**
     * Creates a new instance of this class.
     *
     * @param source the source container of this event
     * @param menuItemSupplier the menu item supplier
     * @param path the path of the menu item
     */
    public AbstractMenuItemContainerMenuItemEvent(MenuItemContainer<MenuItem, Menu, ?> source, MenuItemSupplier<? extends M> menuItemSupplier, List<String> path) {
        super(source);
        this.menuItemSupplier = menuItemSupplier;
        this.path = path;
    }

    /**
     * Gets the path of the menu item.
     *
     * @return the path of the menu item
     */
    public List<String> getPath() {
        return path;
    }

    /**
     * Gets the menu item supplier.
     *
     * @return the menu item supplier
     */
    public MenuItemSupplier<? extends M> getMenuItemSupplier() {
        return menuItemSupplier;
    }
}
