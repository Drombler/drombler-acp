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

    void addMenuItem(MenuItem menuItem, F supplierFactory);

    MenuItemSortingStrategy<MenuItem, F> getMenuItemSortingStrategy(); // TODO: correct? or MenuItemSortingStrategy<MenuItem, ?> ?

    /**
     * @return the menuContainers
     */
    MenuItemContainer<MenuItem, Menu, ?> getMenuContainer(String id);

    MenuItemContainer<MenuItem, Menu, ?> getParentMenuContainer();

    boolean isSupportingItems();

    List<String> getPath();

    String getId();
}
