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
import org.drombler.acp.core.action.jaxb.ToolBarType;
import org.drombler.acp.core.action.spi.impl.ShowToolBarAction;
import org.drombler.acp.core.commons.util.OSGiResourceBundleUtils;
import org.osgi.framework.Bundle;
import org.softsmithy.lib.util.Positionable;

/**
 *
 * @author puce
 */
public class ToolBarDescriptor implements Positionable {

    private String id;
    private String displayName;
    private int position;
    private boolean visible;
    private ToggleActionDescriptor showToolBarActionDescriptor;
    private ToggleMenuEntryDescriptor showToolBarCheckMenuEntryDescriptor;

    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the position
     */
    @Override
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public static <T, B> ToolBarDescriptor createToolBarDescriptor(ToolBarType toolBarType, Bundle bundle, ToolBarContainer<T, B> toolBarContainer) {
        ToolBarDescriptor toolBarDescriptor = new ToolBarDescriptor();

        toolBarDescriptor.setId(StringUtils.stripToNull(toolBarType.getId()));
        toolBarDescriptor.setDisplayName(OSGiResourceBundleUtils.getPackageResourceStringPrefixed(toolBarType.getPackage(),
                toolBarType.getDisplayName(), bundle));
        toolBarDescriptor.setPosition(toolBarType.getPosition());
        toolBarDescriptor.setVisible(toolBarType.isVisible());
        ToggleActionDescriptor actionDescriptor = createShowToolBarActionDescriptor(toolBarDescriptor, toolBarContainer);
        toolBarDescriptor.setShowToolBarActionDescriptor(actionDescriptor);
        toolBarDescriptor.setShowToolBarCheckMenuEntryDescriptor(new ToggleMenuEntryDescriptor(actionDescriptor.getId(),
                "View/Toolbars", toolBarType.getPosition()));
        return toolBarDescriptor;
    }

    private static <T, B> ToggleActionDescriptor createShowToolBarActionDescriptor(ToolBarDescriptor toolBarDescriptor, ToolBarContainer<T, B> toolBarContainer) {
        ToggleActionDescriptor actionDescriptor = new ToggleActionDescriptor();
        actionDescriptor.setId(ShowToolBarAction.class.getName() + "#" + toolBarDescriptor.getId()); // TODO: ok?
        actionDescriptor.setDisplayName(toolBarDescriptor.getDisplayName());
        actionDescriptor.setListener(new ShowToolBarAction<>(toolBarDescriptor.getId(), toolBarContainer));
        return actionDescriptor;
    }

    public ToggleActionDescriptor getShowToolBarActionDescriptor() {
        return showToolBarActionDescriptor;
    }

    /**
     * @param showToolBarActionDescriptor the showToolBarActionDescriptor to set
     */
    public void setShowToolBarActionDescriptor(ToggleActionDescriptor showToolBarActionDescriptor) {
        this.showToolBarActionDescriptor = showToolBarActionDescriptor;
    }

    public ToggleMenuEntryDescriptor getShowToolBarCheckMenuEntryDescriptor() {
        return showToolBarCheckMenuEntryDescriptor;
    }

    /**
     * @param showToolBarCheckMenuEntryDescriptor the showToolBarCheckMenuEntryDescriptor to set
     */
    public void setShowToolBarCheckMenuEntryDescriptor(ToggleMenuEntryDescriptor showToolBarCheckMenuEntryDescriptor) {
        this.showToolBarCheckMenuEntryDescriptor = showToolBarCheckMenuEntryDescriptor;
    }
}
