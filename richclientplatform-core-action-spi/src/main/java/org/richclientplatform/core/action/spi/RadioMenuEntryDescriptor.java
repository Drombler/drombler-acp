/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.richclientplatform.core.action.jaxb.RadioMenuEntryType;

/**
 *
 * @author puce
 */
public class RadioMenuEntryDescriptor extends MenuEntryDescriptor {

    public static RadioMenuEntryDescriptor createRadioMenuEntryDescriptor(RadioMenuEntryType menuEntry) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

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
}
