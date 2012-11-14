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

import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.action.jaxb.ToolBarToggleEntryType;

/**
 *
 * @author puce
 */
public class ToolBarToggleEntryDescriptor extends ToolBarEntryDescriptor {

    private final String toggleGroupId;

    public ToolBarToggleEntryDescriptor(String actionId, String toolBarId, int position, String toggleGroupId) {
        super(actionId, toolBarId, position);
        this.toggleGroupId = toggleGroupId;
    }

    public static ToolBarToggleEntryDescriptor createToolBarToggleEntryDescriptor(ToolBarToggleEntryType toolBarEntryType) {
        return new ToolBarToggleEntryDescriptor(StringUtils.stripToNull(toolBarEntryType.getActionId()),
                StringUtils.stripToNull(toolBarEntryType.getToolBarId()), toolBarEntryType.getPosition(),
                StringUtils.stripToNull(toolBarEntryType.getToggleGroupId()));
    }

    /**
     * @return the toggleGroupId
     */
    public String getToggleGroupId() {
        return toggleGroupId;
    }
}
