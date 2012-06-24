/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import org.richclientplatform.core.action.spi.MenuEntryDescriptor;

/**
 *
 * @author puce
 */
public class MenuItemConfig<Action> {

    private static final int ICON_SIZE = 16;
    private final Action action;

    public MenuItemConfig(Action action) {
        this.action = action;
    }

    /**
     * @return the action
     */
    public Action getAction() {
        return action;
    }

    /**
     * @return the iconSize
     */
    public int getIconSize() {
        return ICON_SIZE;
    }
}
