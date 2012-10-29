/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi;

import java.util.EventListener;

/**
 *TODO: more methods might be added in future
 * @author puce
 */
public interface MenuItemContainerListener<MenuItem, Menu extends MenuItem> extends EventListener {

    void menuAdded(MenuItemContainerMenuEvent<MenuItem, Menu> event);

    void menuItemAdded(MenuItemContainerMenuItemEvent<MenuItem, Menu> event);
}
