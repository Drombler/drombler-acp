/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import java.util.EventListener;

/**
 * TODO: more methods might be added in future
 * @author puce
 */
public interface ToolBarContainerListener<ToolBar, ToolBarButton> extends EventListener {

    void toolBarAdded(ToolBarContainerToolBarEvent<ToolBar, ToolBarButton> event);

    void toolBarButtonAdded(ToolBarContainerToolBarButtonEvent<ToolBar, ToolBarButton> event);
}
