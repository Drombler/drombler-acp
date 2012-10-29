/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi;

/**
 *
 * @author puce
 */
public interface MenuItemContainer<MenuItem, Menu extends MenuItem> {

//    void addMenu(String id, PositionableMenuItemAdapter<? extends M> menu);
    void addMenu(String id, PositionableMenuItemAdapter<? extends Menu> menu);

    void addMenuItem(PositionableMenuItemAdapter<? extends MenuItem> menuItem);

    /**
     * @return the menuContainers
     */
    MenuItemContainer<MenuItem, Menu> getMenuContainer(String id);

    boolean isSupportingItems();
}
