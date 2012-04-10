/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.processing;

/**
 *
 * @author puce
 */
public interface MenuFactory<T> {

    T createMenu(MenuDescriptor menuDescriptor);
}
