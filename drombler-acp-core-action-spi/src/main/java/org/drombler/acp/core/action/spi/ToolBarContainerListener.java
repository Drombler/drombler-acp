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

import java.util.EventListener;

/**
 * A {@link ToolBarContainer} listener.<br>
 * <br>
 * Note: more methods might be added in future
 *
 * @param <ToolBar> the GUI-toolkit specific type of a tool bar
 * @param <ToolBarButton> the GUI-toolkit specific base type of a tool bar button.
 */
public interface ToolBarContainerListener<ToolBar, ToolBarButton> extends EventListener {

    /**
     * The callback method when a tool bar was added.
     *
     * @param event the event
     */
    void toolBarAdded(ToolBarContainerToolBarEvent<ToolBar, ToolBarButton> event);

    /**
     * The callback method when a tool bar was removed.
     *
     * @param event the event
     */
    void toolBarRemoved(ToolBarContainerToolBarEvent<ToolBar, ToolBarButton> event);

    /**
     * The callback method when a tool bar button was added.
     *
     * @param event the event
     */
    void toolBarButtonAdded(ToolBarContainerToolBarButtonEvent<ToolBar, ToolBarButton> event);

    /**
     * The callback method when a tool bar button was removed.
     *
     * @param event the event
     */
    void toolBarButtonRemoved(ToolBarContainerToolBarButtonEvent<ToolBar, ToolBarButton> event);
}
