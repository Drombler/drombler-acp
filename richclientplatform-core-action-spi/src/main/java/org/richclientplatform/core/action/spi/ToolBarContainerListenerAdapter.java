/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

/**
 *
 * @author puce
 */
public class ToolBarContainerListenerAdapter<ToolBar, ToolBarButton> implements ToolBarContainerListener<ToolBar, ToolBarButton> {

    @Override
    public void toolBarAdded(ToolBarContainerToolBarEvent<ToolBar, ToolBarButton> event) {
        // do nothing
    }

    @Override
    public void toolBarButtonAdded(ToolBarContainerToolBarButtonEvent<ToolBar, ToolBarButton> event) {
        // do nothing
    }
}
