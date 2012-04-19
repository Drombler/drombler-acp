/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.apache.commons.lang.StringUtils;
import org.richclientplatform.core.action.jaxb.CheckMenuEntryType;

/**
 *
 * @author puce
 */
public class CheckMenuEntryDescriptor extends MenuEntryDescriptor {

    public static CheckMenuEntryDescriptor createCheckMenuEntryDescriptor(CheckMenuEntryType menuEntryType) {
        return new CheckMenuEntryDescriptor(StringUtils.stripToNull(menuEntryType.getActionId()),
                StringUtils.stripToEmpty(menuEntryType.getPath()), menuEntryType.getPosition());
    }

    public CheckMenuEntryDescriptor(String actionId, String path, int position) {
        super(actionId, path, position);
    }
}
