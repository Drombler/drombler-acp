/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

/**
 *
 * @author puce
 */
public interface ToolBarButtonFactory<B, A> {

    B createToolBarButton(ToolBarEntryDescriptor toolBarEntryDescriptor, A action, int iconSize);
}
