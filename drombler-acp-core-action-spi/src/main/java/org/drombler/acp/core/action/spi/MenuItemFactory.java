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
 * A menu item factory to create a GUI toolkit specific menu item component from an action.<br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @author puce
 * @param <T> the GUI toolkit specific type for menu items
 * @param <A> the action type
 */
public interface MenuItemFactory<T, A> {

    /**
     * Creates a GUI toolkit specific menu item component from the specified action.
     *
     * @param action the action for the menu item
     * @param iconSize the icon size
     * @return a new menu item
     */
    T createMenuItem(A action, int iconSize);
}
