/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi;

import org.drombler.acp.core.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
public class ToolBarContainerToolBarButtonEvent<ToolBar, ToolBarButton> extends AbstractToolBarContainerEvent<ToolBar, ToolBarButton, ToolBarButton> {

    public ToolBarContainerToolBarButtonEvent(ToolBarContainer<ToolBar, ToolBarButton> source, String toolBarId, PositionableAdapter<? extends ToolBarButton> entry) {
        super(source, toolBarId, entry);
    }
}
