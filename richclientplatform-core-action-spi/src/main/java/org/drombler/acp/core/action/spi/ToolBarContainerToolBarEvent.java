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
public class ToolBarContainerToolBarEvent<ToolBar, ToolBarButton> extends AbstractToolBarContainerEvent<ToolBar, ToolBarButton, ToolBar> {

    public ToolBarContainerToolBarEvent(ToolBarContainer<ToolBar, ToolBarButton> source, String toolBarId, PositionableAdapter<? extends ToolBar> entry) {
        super(source, toolBarId, entry);
    }
}
