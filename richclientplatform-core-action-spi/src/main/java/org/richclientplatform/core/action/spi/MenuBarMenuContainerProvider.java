/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

/**
 *
 * @author puce
 */
public interface MenuBarMenuContainerProvider<MenuItem, Menu extends MenuItem> {

    MenuItemRootContainer<MenuItem, Menu> getMenuBarMenuContainer();
}
