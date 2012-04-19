/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.apache.commons.lang.StringUtils;
import org.richclientplatform.core.action.jaxb.RadioMenuEntryType;

/**
 *
 * @author puce
 */
public class RadioMenuEntryDescriptor extends MenuEntryDescriptor {

    private final String toggleGroupId;

    public RadioMenuEntryDescriptor(String actionId, String path, int position, String toggleGroupId) {
        super(actionId, path, position);
        this.toggleGroupId = toggleGroupId;
    }

    /**
     * @return the toggleGroupId
     */
    public String getToggleGroupId() {
        return toggleGroupId;
    }

    public static RadioMenuEntryDescriptor createRadioMenuEntryDescriptor(RadioMenuEntryType menuEntryType) {
        return new RadioMenuEntryDescriptor(StringUtils.stripToNull(menuEntryType.getActionId()),
                StringUtils.stripToEmpty(menuEntryType.getPath()), menuEntryType.getPosition(),
                StringUtils.stripToNull(menuEntryType.getToggleGroupId()));
    }
}
