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
import org.drombler.acp.core.action.jaxb.ToolBarEntryType;
import org.softsmithy.lib.util.Positionable;

/**
 * A tool bar entry descriptor.
 *
 * @author puce
 */
public class ToolBarEntryDescriptor implements Positionable {

    private final String actionId;
    private final String toolBarId;
    private final int position;

    /**
     * Creates a new instance of this class.
     *
     * @param actionId the id of the action for this tool bar entry
     * @param toolBarId the tool bar id of the tool bar this entry should be added to
     * @param position the preferred position of the tool bar entry in the specified tool bar
     */
    public ToolBarEntryDescriptor(String actionId, String toolBarId, int position) {
        this.actionId = actionId;
        this.toolBarId = toolBarId;
        this.position = position;
    }

    /**
     * Gets the id of the action for this tool bar entry.
     *
     * @return the id of the action for this tool bar entry
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * Gets the tool bar id of the tool bar this entry should be added to.
     *
     * @return the tool bar id of the tool bar this entry should be added to
     */
    public String getToolBarId() {
        return toolBarId;
    }

    /**
     * Gets the preferred position of the tool bar entry in the tool bar.
     *
     * @return the preferred position of the tool bar entry in the tool bar
     */
    @Override
    public int getPosition() {
        return position;
    }

    /**
     * Creates an instance of a {@link ToolBarEntryDescriptor} from a {@link ToolBarEntryType} unmarshalled from the application.xml.
     *
     * @param toolBarEntryType the unmarshalled ToolBarEntryType
     * @return a ToolBarEntryDescriptor
     */
    public static ToolBarEntryDescriptor createToolBarEntryDescriptor(ToolBarEntryType toolBarEntryType) {
        return new ToolBarEntryDescriptor(StringUtils.stripToNull(toolBarEntryType.getActionId()),
                StringUtils.stripToNull(toolBarEntryType.getToolBarId()), toolBarEntryType.getPosition());
    }

}
