/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import java.util.List;

/**
 *
 * @author puce
 */
public class MenuItemContainerMenuItemEvent<MenuItem, Menu extends MenuItem> extends AbstractMenuItemContainerMenuItemEvent<MenuItem, Menu, MenuItem> {

    public MenuItemContainerMenuItemEvent(MenuItemContainer<MenuItem, Menu> source, PositionableMenuItemAdapter<? extends MenuItem> menuItem, List<String> path) {
        super(source, menuItem, path);
    }
}
