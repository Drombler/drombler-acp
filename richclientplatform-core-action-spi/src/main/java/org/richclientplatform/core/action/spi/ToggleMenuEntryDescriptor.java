/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.apache.commons.lang.StringUtils;
import org.richclientplatform.core.action.jaxb.ToggleMenuEntryType;

/**
 *
 * @author puce
 */
public class ToggleMenuEntryDescriptor extends MenuEntryDescriptor {

    private final String toggleGroupId;

    public ToggleMenuEntryDescriptor(String actionId, String toggleGroupId, String path, int position) {
        super(actionId, path, position);
        this.toggleGroupId = toggleGroupId;
    }

    public ToggleMenuEntryDescriptor(String actionId, String path, int position) {
        this(actionId, null, path, position);
    }

    /**
     * @return the toggleGroupId
     */
    public String getToggleGroupId() {
        return toggleGroupId;
    }

    public static ToggleMenuEntryDescriptor createRadioMenuEntryDescriptor(ToggleMenuEntryType menuEntryType) {
        return new ToggleMenuEntryDescriptor(StringUtils.stripToNull(menuEntryType.getActionId()),
                StringUtils.stripToNull(menuEntryType.getToggleGroupId()),
                StringUtils.stripToEmpty(menuEntryType.getPath()), menuEntryType.getPosition());
    }
}
