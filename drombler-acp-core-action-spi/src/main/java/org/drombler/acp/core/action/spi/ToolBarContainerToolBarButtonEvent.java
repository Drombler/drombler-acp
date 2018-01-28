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

import org.softsmithy.lib.util.PositionableAdapter;

/**
 * A {@link ToolBarContainer} tool bar button event.
 *
 * @param <ToolBar> the GUI-toolkit specific type of a tool bar
 * @param <ToolBarButton> the GUI-toolkit specific base type of a tool bar button.
 */
public class ToolBarContainerToolBarButtonEvent<ToolBar, ToolBarButton> extends AbstractToolBarContainerEvent<ToolBar, ToolBarButton, ToolBarButton> {

    private static final long serialVersionUID = 7597269062649584187L;

    /**
     * Creates a new instance of this class
     *
     * @param source the source container
     * @param toolBarId the tool bar id
     * @param entry the tool bar button
     */
    public ToolBarContainerToolBarButtonEvent(ToolBarContainer<ToolBar, ToolBarButton> source, String toolBarId, PositionableAdapter<? extends ToolBarButton> entry) {
        super(source, toolBarId, entry);
    }
}
