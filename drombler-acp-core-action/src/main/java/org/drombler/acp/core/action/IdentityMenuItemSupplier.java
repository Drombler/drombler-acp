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
package org.drombler.acp.core.action;

/**
 * This simple {@link MenuItemSupplier} just provides the menu item given at construction time.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @see MenuItemSortingStrategy
 * @see TextMenuItemSortingStrategy
 * @author puce
 */
// TODO: good name?
public class IdentityMenuItemSupplier<MenuItem> implements MenuItemSupplier<MenuItem> {

    private final MenuItem menuItem;

    /**
     * Creates a new instance of this class.
     *
     * @param menuItem the menu item to provide with {@link #getMenuItem() }
     */
    public IdentityMenuItemSupplier(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MenuItem getMenuItem() {
        return menuItem;
    }

}
