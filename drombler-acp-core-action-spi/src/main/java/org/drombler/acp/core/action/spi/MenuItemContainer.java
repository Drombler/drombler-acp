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
import org.drombler.acp.core.action.MenuItemSortingStrategy;
import org.drombler.acp.core.action.MenuItemSupplierFactory;

/**
 * A menu item container.<br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @param <F> the sorting strategy specific menu item supplier factory type
 * @author puce
 */
public interface MenuItemContainer<MenuItem, Menu extends MenuItem, F extends MenuItemSupplierFactory<MenuItem, F>> {

    /**
     * Adds a sub-menu to this menu item container.
     *
     * @param id the id of the sub-menu
     * @param menu the sub-menu to add
     * @param supplierFactory the {@link MenuItemSupplierFactory} of this container
     * @param sortingStrategy the menu item sorting strategy of the sub-menu
     */
    void addMenu(String id, Menu menu, F supplierFactory, MenuItemSortingStrategy<MenuItem, ?> sortingStrategy);

    /**
     * Adds a menu item to this menu item container.
     *
     * @param menuItem the menu item to add
     * @param supplierFactory the {@link MenuItemSupplierFactory} of this container
     */
    void addMenuItem(MenuItem menuItem, F supplierFactory);

    /**
     * Gets the sorting strategy.
     *
     * @return the sorting strategy
     */
    MenuItemSortingStrategy<MenuItem, F> getMenuItemSortingStrategy(); // TODO: correct? or MenuItemSortingStrategy<MenuItem, ?> ?

    /**
     * Gets the menu item container for the sub-menu with the specified id.
     *
     * @param id the id of the sub-menu
     * @return the menuContainers the menu item container for the sub-menu with the specified id
     */
    MenuItemContainer<MenuItem, Menu, ?> getMenuContainer(String id);

    /**
     * Gets the parent menu container, if any, else null.
     *
     * @return the parent menu container, if any, else null
     */
    MenuItemContainer<MenuItem, Menu, ?> getParentMenuContainer();

    /**
     * Flag which indicates if this menu item container also supports menu items or just menus.
     *
     * @return true, if this menu item container also supports menu item, false if it only supports menus.
     */
    boolean isSupportingItems();

    /**
     * Gets the path of menu ids.
     *
     * @return the path of menu ids
     */
    List<String> getPath();

    /**
     * Gets the id (or null for root containers).
     *
     * @return the id (or null for root containers)
     */
    String getId();
}
