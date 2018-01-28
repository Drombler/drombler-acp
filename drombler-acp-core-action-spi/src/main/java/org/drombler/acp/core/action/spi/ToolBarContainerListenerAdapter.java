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
package org.drombler.acp.core.action.spi;

/**
 * An base class for {@link ToolBarContainerListener}s.
 *
 * The implemented methods do nothing by default.
 *
 * @param <ToolBar> the GUI-toolkit specific type of a tool bar
 * @param <ToolBarButton> the GUI-toolkit specific base type of a tool bar button.
 */
public class ToolBarContainerListenerAdapter<ToolBar, ToolBarButton> implements ToolBarContainerListener<ToolBar, ToolBarButton> {

    /**
     * {@inheritDoc }
     *
     * This implementation does nothing by default.
     */
    @Override
    public void toolBarAdded(ToolBarContainerToolBarEvent<ToolBar, ToolBarButton> event) {
        // do nothing
    }

    /**
     * {@inheritDoc }
     *
     * This implementation does nothing by default.
     */
    @Override
    public void toolBarRemoved(ToolBarContainerToolBarEvent<ToolBar, ToolBarButton> event) {
        // do nothing
    }

    /**
     * {@inheritDoc }
     *
     * This implementation does nothing by default.
     */
    @Override
    public void toolBarButtonAdded(ToolBarContainerToolBarButtonEvent<ToolBar, ToolBarButton> event) {
        // do nothing
    }

    /**
     * {@inheritDoc }
     *
     * This implementation does nothing by default.
     */
    @Override
    public void toolBarButtonRemoved(ToolBarContainerToolBarButtonEvent<ToolBar, ToolBarButton> event) {
        // do nothing
    }
}
