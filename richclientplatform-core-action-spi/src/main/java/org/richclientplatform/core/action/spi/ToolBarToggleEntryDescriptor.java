/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.apache.commons.lang.StringUtils;
import org.richclientplatform.core.action.jaxb.ToolBarToggleEntryType;

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
