/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import org.richclientplatform.core.action.AbstractToggleActionListener;
import org.richclientplatform.core.action.spi.ToolBarContainer;

/**
 *
 * @author puce
 */
public class ShowToolBarAction extends AbstractToggleActionListener<Object> {
    private final String toolBarId;
    private final ToolBarContainer<?, ?> toolBarContainer;

    public ShowToolBarAction(String toolBarId, ToolBarContainer<?, ?> toolBarContainer) {
        this.toolBarId = toolBarId;
        this.toolBarContainer = toolBarContainer;
//        setSelected(toolBarContainer.isToolBarVisible(toolBarId));
    }

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
         toolBarContainer.setToolBarVisible(toolBarId, newValue);
    }
    
}
