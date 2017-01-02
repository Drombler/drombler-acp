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

import org.softsmithy.lib.util.PositionableAdapter;

/**
 * This {@link MenuItemSupplier} associates a menu item with a position. The position is needed by the {@link PositionSortingStrategy}.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @see PositionSortingStrategy
 * @author puce
 */
public class PositionableMenuItemAdapter<MenuItem> extends PositionableAdapter<MenuItem> implements MenuItemSupplier<MenuItem> {

    /**
     * Creates a new instance of this class.
     *
     * @param menuItem the menu item
     * @param position the position
     */
    public PositionableMenuItemAdapter(MenuItem menuItem, int position) {
        super(menuItem, position);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MenuItem getMenuItem() {
        return getAdapted();
    }
}
