/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import java.util.EventListener;

/**
 *
 * @author puce
 */
public interface MenuItemContainerListener<MenuItem, Menu extends MenuItem> extends EventListener {

    void menuAdded(MenuItemContainerMenuEvent<MenuItem, Menu> event);

    void menuItemAdded(MenuItemContainerMenuItemEvent<MenuItem, Menu> event);
}
