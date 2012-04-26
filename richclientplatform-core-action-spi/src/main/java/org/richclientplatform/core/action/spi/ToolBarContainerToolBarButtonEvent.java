/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.richclientplatform.core.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
public class ToolBarContainerToolBarButtonEvent<ToolBar, ToolBarButton> extends AbstractToolBarContainerEvent<ToolBar, ToolBarButton, ToolBarButton> {

    public ToolBarContainerToolBarButtonEvent(ToolBarContainer<ToolBar, ToolBarButton> source, String toolBarId, PositionableAdapter<? extends ToolBarButton> entry) {
        super(source, toolBarId, entry);
    }
}
