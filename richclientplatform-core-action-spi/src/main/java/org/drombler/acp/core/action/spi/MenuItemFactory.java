/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi;

/**
 *
 * @author puce
 */
public interface MenuItemFactory<T, A> {

    T createMenuItem(MenuEntryDescriptor menuEntryDescriptor, A action, int iconSize);
}
