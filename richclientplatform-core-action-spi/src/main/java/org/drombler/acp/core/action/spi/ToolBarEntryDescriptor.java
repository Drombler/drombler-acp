/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
