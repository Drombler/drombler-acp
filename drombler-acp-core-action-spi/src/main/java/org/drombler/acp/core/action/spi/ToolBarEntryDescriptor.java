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

import org.apache.commons.lang.StringUtils;
import org.drombler.acp.core.action.jaxb.ToolBarEntryType;
import org.drombler.acp.core.lib.util.Positionable;

/**
 *
 * @author puce
 */
public class ToolBarEntryDescriptor implements Positionable {

    private final String actionId;
    private final String toolBarId;
    private final int position;

    public ToolBarEntryDescriptor(String actionId, String toolBarId, int position) {
        this.actionId = actionId;
        this.toolBarId = toolBarId;
        this.position = position;
    }

    public String getActionId() {
        return actionId;
    }

    public static ToolBarEntryDescriptor createToolBarEntryDescriptor(ToolBarEntryType toolBarEntryType) {
        return new ToolBarEntryDescriptor(StringUtils.stripToNull(toolBarEntryType.getActionId()),
                StringUtils.stripToNull(toolBarEntryType.getToolBarId()), toolBarEntryType.getPosition());
    }

    /**
     * @return the toolBarId
     */
    public String getToolBarId() {
        return toolBarId;
    }

    /**
     * @return the position
     */
    @Override
    public int getPosition() {
        return position;
    }
}
