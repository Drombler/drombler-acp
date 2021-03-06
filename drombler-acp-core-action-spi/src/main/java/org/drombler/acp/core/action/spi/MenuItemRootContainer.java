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

import org.drombler.acp.core.action.MenuItemSupplierFactory;

/**
 * A menu item root container.<br>
 * <br>
 *
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @param <F> the sorting strategy specific menu item supplier factory type
 * @author puce
 */
public interface MenuItemRootContainer<MenuItem, Menu extends MenuItem, F extends MenuItemSupplierFactory<MenuItem, F>> extends MenuItemContainer<MenuItem, Menu, F> {

    /**
     * Adds a {@link MenuItemContainerListener}.
     *
     * @param containerListener the container listener to add
     */
    void addMenuContainerListener(MenuItemContainerListener<MenuItem, Menu> containerListener);

    /**
     * Removes a {@link MenuItemContainerListener}.
     *
     * @param containerListener the container listener to remove
     */
    void removeMenuContainerListener(MenuItemContainerListener<MenuItem, Menu> containerListener);
}
