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
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @author puce
 */
public class PositionableMenuItemAdapter<MenuItem> extends PositionableAdapter<MenuItem> implements MenuItemSupplier<MenuItem> {

    private final boolean separator;

    public PositionableMenuItemAdapter(MenuItem menuItem, int position, boolean separator) {
        super(menuItem, position);
        this.separator = separator;
    }

    public boolean isSeparator() {
        return separator;
    }

    @Override
    public MenuItem getMenuItem() {
        return getAdapted();
    }
}
