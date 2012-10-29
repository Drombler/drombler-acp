/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi.impl;

import org.drombler.acp.core.action.AbstractToggleActionListener;
import org.drombler.acp.core.action.spi.ToolBarContainer;
import org.drombler.acp.core.action.spi.ToolBarContainerListenerAdapter;
import org.drombler.acp.core.action.spi.ToolBarContainerToolBarEvent;

/**
 *
 * @author puce
 */
public class ShowToolBarAction<ToolBar, ToolBarButton> extends AbstractToggleActionListener<Object> {

    private final String toolBarId;
    private final ToolBarContainer<ToolBar, ToolBarButton> toolBarContainer;

    public ShowToolBarAction(final String toolBarId, ToolBarContainer<ToolBar, ToolBarButton> toolBarContainer) {
        this.toolBarId = toolBarId;
        this.toolBarContainer = toolBarContainer;
        toolBarContainer.addToolBarContainerListener(toolBarId,
                new ToolBarContainerListenerAdapter<ToolBar, ToolBarButton>() {

                    @Override
                    public void toolBarAdded(ToolBarContainerToolBarEvent<ToolBar, ToolBarButton> event) {
                        setSelected(ShowToolBarAction.this.toolBarContainer.isToolBarVisible(toolBarId));
                    }
                });
    }

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        toolBarContainer.setToolBarVisible(toolBarId, newValue);
    }
}
