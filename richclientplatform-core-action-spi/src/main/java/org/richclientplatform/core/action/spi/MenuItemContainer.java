/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

/**
 *
 * @author puce
 */
public interface MenuItemContainer<M, I> {

//    void addMenu(String id, PositionableMenuItemAdapter<? extends M> menu);
    void addMenu(String id, PositionableMenuItemAdapter<? extends M> menu);

    void addMenuItem(PositionableMenuItemAdapter<? extends I> menuItem);

    /**
     * @return the menuContainers
     */
    MenuItemContainer<M, I> getMenuContainer(String id);

    boolean isSupportingItems();
}
