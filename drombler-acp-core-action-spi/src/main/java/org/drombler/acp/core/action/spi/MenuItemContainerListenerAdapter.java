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

/**
 * A simple implementation of {@link MenuItemContainerListener}, which does nothing by default.<br>
 * <br>
 * Subclasses only need to implement the methods they are interested in.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @author puce
 */
public class MenuItemContainerListenerAdapter<MenuItem, Menu extends MenuItem> implements MenuItemContainerListener<MenuItem, Menu> {

    /**
     * {@inheritDoc }
     *
     * Does nothing by default.
     *
     */
    @Override
    public void menuAdded(MenuItemContainerMenuEvent<MenuItem, Menu> event) {
        // do nothing
    }

    /**
     * {@inheritDoc }
     *
     * Does nothing by default.
     *
     */
    @Override
    public void menuItemAdded(MenuItemContainerMenuItemEvent<MenuItem, Menu> event) {
        // do nothing
    }
}
