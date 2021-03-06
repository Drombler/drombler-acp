/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2012 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.action.spi.impl;

import org.drombler.acp.core.action.spi.ToolBarContainer;
import org.drombler.acp.core.action.spi.ToolBarContainerListenerAdapter;
import org.drombler.acp.core.action.spi.ToolBarContainerToolBarEvent;
import org.drombler.commons.action.AbstractToggleActionListener;

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
                new ToolBarContainerListenerAdapter<>() {

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
