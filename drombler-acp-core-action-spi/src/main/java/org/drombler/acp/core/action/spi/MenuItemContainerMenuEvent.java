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

/**
 *
 * @author puce
 */
public class MenuItemContainerMenuEvent<MenuItem, Menu extends MenuItem> extends AbstractMenuItemContainerMenuItemEvent<MenuItem, Menu, Menu> {

    private final String menuId;

    public MenuItemContainerMenuEvent(MenuItemContainer<MenuItem, Menu, ?> source, MenuItemSupplier<? extends Menu> menu, String menuId, List<String> path) {
        super(source, menu, path);
        this.menuId = menuId;
    }

    /**
     * @return the menuId
     */
    public String getMenuId() {
        return menuId;
    }
}
