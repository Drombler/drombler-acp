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

import java.util.EventObject;
import org.drombler.acp.core.commons.util.PositionableAdapter;

/**
 *
 * @author puce
 */
public abstract class AbstractToolBarContainerEvent<ToolBar, ToolBarButton, T> extends EventObject {

    private final String toolBarId;
    private final PositionableAdapter<? extends T> entry;

    public AbstractToolBarContainerEvent(ToolBarContainer<ToolBar, ToolBarButton> source, String toolBarId, PositionableAdapter<? extends T> entry) {
        super(source);
        this.toolBarId = toolBarId;
        this.entry = entry;
    }

    /**
     * @return the toolBarId
     */
    public String getToolBarId() {
        return toolBarId;
    }

    /**
     * @return the menuItem
     */
    public PositionableAdapter<? extends T> getEntry() {
        return entry;
    }
}
