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
 * A toggle menu item factory to create a GUI toolkit specific toggle menu item component from a toggle action.<br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @author puce
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <ToggleMenuItem> the GUI toolkit specific type for toggle menu items
 * @param <ToggleAction> the toggle action type
 */
public interface ToggleMenuItemFactory<MenuItem, ToggleMenuItem extends MenuItem, ToggleAction> {

    /**
     * Creates a GUI toolkit specific toggle menu item component from the specified toggle action.
     *
     * @param toggleMenuEntryDescriptor the toggle menu entry descriptor
     * @param toggleAction the toggle action for the toggle menu item
     * @param iconSize the icon size
     * @return a new toggle menu item
     */
    ToggleMenuItem createToggleMenuItem(ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem, ?> toggleMenuEntryDescriptor, ToggleAction toggleAction, int iconSize);
}
