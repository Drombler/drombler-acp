/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi;

import java.util.EventObject;
import java.util.List;

/**
 *
 * @author puce
 */
public abstract class AbstractMenuItemContainerMenuItemEvent<MenuItem, Menu extends MenuItem, M extends MenuItem> extends EventObject {

    private final PositionableMenuItemAdapter<? extends M> menuItem;
    private final List<String> path;

    public AbstractMenuItemContainerMenuItemEvent(MenuItemContainer<MenuItem, Menu> source, PositionableMenuItemAdapter<? extends M> menuItem, List<String> path) {
        super(source);
        this.menuItem = menuItem;
        this.path = path;
    }

    /**
     * @return the path
     */
    public List<String> getPath() {
        return path;
    }

    /**
     * @return the menuItem
     */
    public PositionableMenuItemAdapter<? extends M> getMenuItem() {
        return menuItem;
    }
}
