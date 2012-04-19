/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

/**
 *
 * @author puce
 */
public interface ToolBarToggleButtonFactory<B, A> {

    B createToolToggleBarButton(ToolBarToggleEntryDescriptor toolBarToggleEntryDescriptor, A action, int iconSize);
}
