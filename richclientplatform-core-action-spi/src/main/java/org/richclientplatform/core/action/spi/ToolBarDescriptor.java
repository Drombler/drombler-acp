/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.richclientplatform.core.action.jaxb.MenuType;
import org.richclientplatform.core.action.jaxb.ToolBarType;
import org.richclientplatform.core.lib.util.Resources;
import org.richclientplatform.core.lib.util.Positionable;

/**
 *
 * @author puce
 */
public class ToolBarDescriptor implements Positionable {

    private final String id;
    private final String displayName;
    private final int position;

    public ToolBarDescriptor(String id, String displayName, int position) {
        this.id = id;
        this.displayName = displayName;
        this.position = position;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public static ToolBarDescriptor createToolBarDescriptor(ToolBarType toolBarType, Bundle bundle) {
        return new ToolBarDescriptor(StringUtils.stripToNull(toolBarType.getId()),
                Resources.getResourceString(toolBarType.getPackage(), toolBarType.getDisplayName(), bundle),
                toolBarType.getPosition());
    }

    /**
     * @return the position
     */
    @Override
    public int getPosition() {
        return position;
    }
}
