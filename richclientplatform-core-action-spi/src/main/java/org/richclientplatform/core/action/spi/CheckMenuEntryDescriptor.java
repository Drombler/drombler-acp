/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.richclientplatform.core.action.jaxb.CheckMenuEntryType;

/**
 *
 * @author puce
 */
public class CheckMenuEntryDescriptor extends MenuEntryDescriptor {

    public static CheckMenuEntryDescriptor createCheckMenuEntryDescriptor(CheckMenuEntryType menuEntry) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public CheckMenuEntryDescriptor(String actionId, String path, int position, String toggleGroupId) {
        super(actionId, path, position);
    }
}
