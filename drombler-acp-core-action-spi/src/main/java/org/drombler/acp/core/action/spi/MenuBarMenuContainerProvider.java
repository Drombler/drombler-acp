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

import org.drombler.acp.core.action.PositionableMenuItemAdapterFactory;

/**
 * The provider which provides the menu bar based menu container.<br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @author puce
 */
public interface MenuBarMenuContainerProvider<MenuItem, Menu extends MenuItem> {

    /**
     * Gets the menu bar based menu container.
     *
     * @return the menu bar based menu container
     */
    MenuItemRootContainer<MenuItem, Menu, PositionableMenuItemAdapterFactory<MenuItem>> getMenuBarMenuContainer();
}
