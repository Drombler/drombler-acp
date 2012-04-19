/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

/**
 *
 * @author puce
 */
public interface CheckMenuItemFactory<T, A> {

    T createCheckMenuItem(MenuEntryDescriptor menuEntryDescriptor, A action, int iconSize);
}
