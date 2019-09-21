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
 * A tool bar toggle entry descriptor.
 *
 * @author puce
 */
public class ToolBarToggleEntryDescriptor extends ToolBarEntryDescriptor {

    private final String toggleGroupId;

    /**
     * Creates a new instance of this class.
     *
     * @param actionId the id of the toggle action for this tool bar toggle entry
     * @param toolBarId the tool bar id of the tool bar this entry should be added to
     * @param position the preferred position of the tool bar entry in the specified tool bar
     * @param toggleGroupId the id of a toggle group
     */
    public ToolBarToggleEntryDescriptor(String actionId, String toolBarId, int position, String toggleGroupId) {
        super(actionId, toolBarId, position);
        this.toggleGroupId = toggleGroupId;
    }

    /**
     * Gets the id of a toggle group. Toggle groups are managed implicitly. All tool bar toggle entries with the same toggle group id are grouped in the same toggle group. If no group id is provided
     * then the tool bar toggle entry won't be part of a toggle group.
     *
     * @return the id of a toggle group
     */
    public String getToggleGroupId() {
        return toggleGroupId;
    }

    /**
     * Creates an instance of a {@link ToolBarToggleEntryDescriptor} from a {@link ToolBarToggleEntryType} unmarshalled from the application.xml.
     *
     * @param toolBarToggleEntryType the unmarshalled ToolBarToggleEntryType
     * @return a ToolBarToggleEntryDescriptor
     */
    public static ToolBarToggleEntryDescriptor createToolBarToggleEntryDescriptor(ToolBarToggleEntryType toolBarToggleEntryType) {
        return new ToolBarToggleEntryDescriptor(StringUtils.stripToNull(toolBarToggleEntryType.getActionId()),
                StringUtils.stripToNull(toolBarToggleEntryType.getToolBarId()), toolBarToggleEntryType.getPosition(),
                StringUtils.stripToNull(toolBarToggleEntryType.getToggleGroupId()));
    }
}
