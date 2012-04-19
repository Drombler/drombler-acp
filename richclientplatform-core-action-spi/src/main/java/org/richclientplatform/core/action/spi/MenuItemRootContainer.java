/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

/**
 *
 * @author puce
 */
public interface MenuItemRootContainer<MenuItem, Menu extends MenuItem> extends MenuItemContainer<MenuItem, Menu> {

    void addMenuContainerListener(MenuItemContainerListener<MenuItem, Menu> containerListener);

    void removeMenuContainerListener(MenuItemContainerListener<MenuItem, Menu> containerListener);
}
