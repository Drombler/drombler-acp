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

import java.util.EventListener;

/**
 * A listener for {@link MenuItemContainerMenuEvent}s .<br>
 * <br>
 * Note: more methods might be added in future
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @author puce
 */
public interface MenuItemContainerListener<MenuItem, Menu extends MenuItem> extends EventListener {

    /**
     * Callback for menu added events.
     *
     * @param event the menu added event
     */
    void menuAdded(MenuItemContainerMenuEvent<MenuItem, Menu> event);

    /**
     * Callback for menu item added events.
     *
     * @param event the menu item added event
     */
    void menuItemAdded(MenuItemContainerMenuItemEvent<MenuItem, Menu> event);
}
