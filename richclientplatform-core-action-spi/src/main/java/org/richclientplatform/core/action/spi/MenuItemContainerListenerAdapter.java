/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

/**
 *
 * @author puce
 */
public class MenuItemContainerListenerAdapter<MenuItem, Menu extends MenuItem> implements MenuItemContainerListener<MenuItem, Menu> {

    @Override
    public void menuAdded(MenuItemContainerMenuEvent<MenuItem, Menu> event) {
        // do nothing
    }

    @Override
    public void menuItemAdded(MenuItemContainerMenuItemEvent<MenuItem, Menu> event) {
        // do nothing
    }
}
