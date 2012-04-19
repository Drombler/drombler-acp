/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import java.util.EventObject;
import java.util.List;

/**
 *
 * @author puce
 */
public class MenuItemContainerMenuEvent<MenuItem, Menu extends MenuItem> extends AbstractMenuItemContainerMenuItemEvent<MenuItem, Menu, Menu> {

    private final String menuId;

    public MenuItemContainerMenuEvent(MenuItemContainer<MenuItem, Menu> source, PositionableMenuItemAdapter<? extends Menu> menu, String menuId, List<String> path) {
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
