/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

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

    /**
     * @return the toggleGroupId
     */
    public String getToggleGroupId() {
        return toggleGroupId;
    }
}
