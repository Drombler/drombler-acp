/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.richclientplatform.core.action.jaxb.ToolBarType;
import org.richclientplatform.core.action.spi.impl.ShowToolBarAction;
import org.richclientplatform.core.lib.util.Positionable;
import org.richclientplatform.core.lib.util.Resources;

/**
 *
 * @author puce
 */
public class ToolBarDescriptor implements Positionable {

    private String id;
    private String displayName;
    private int position;
    private boolean visible;
    private CheckActionDescriptor showToolBarActionDescriptor;
    private CheckMenuEntryDescriptor showToolBarCheckMenuEntryDescriptor;

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
        toolBarDescriptor.setDisplayName(Resources.getResourceString(toolBarType.getPackage(),
                toolBarType.getDisplayName(), bundle));
        toolBarDescriptor.setPosition(toolBarType.getPosition());
        toolBarDescriptor.setVisible(toolBarType.isVisible());
        CheckActionDescriptor actionDescriptor = createShowToolBarActionDescriptor(toolBarDescriptor, toolBarContainer);
        toolBarDescriptor.setShowToolBarActionDescriptor(actionDescriptor);
        toolBarDescriptor.setShowToolBarCheckMenuEntryDescriptor(new CheckMenuEntryDescriptor(actionDescriptor.getId(),
                "View/Toolbars", toolBarType.getPosition()));
        return toolBarDescriptor;
    }

    private static <T, B> CheckActionDescriptor createShowToolBarActionDescriptor(ToolBarDescriptor toolBarDescriptor, ToolBarContainer<T, B> toolBarContainer) {
        ToggleActionDescriptor actionDescriptor = new ToggleActionDescriptor();
        actionDescriptor.setId(ShowToolBarAction.class.getName() + "#" + toolBarDescriptor.getId()); // TODO: ok?
        actionDescriptor.setDisplayName(toolBarDescriptor.getDisplayName());
        actionDescriptor.setListener(new ShowToolBarAction(toolBarDescriptor.getId(), toolBarContainer));
        return actionDescriptor;
    }

    public CheckActionDescriptor getShowToolBarActionDescriptor() {
        return showToolBarActionDescriptor;
    }

    /**
     * @param showToolBarActionDescriptor the showToolBarActionDescriptor to set
     */
    public void setShowToolBarActionDescriptor(CheckActionDescriptor showToolBarActionDescriptor) {
        this.showToolBarActionDescriptor = showToolBarActionDescriptor;
    }

    public CheckMenuEntryDescriptor getShowToolBarCheckMenuEntryDescriptor() {
        return showToolBarCheckMenuEntryDescriptor;
    }

    /**
     * @param showToolBarCheckMenuEntryDescriptor the showToolBarCheckMenuEntryDescriptor to set
     */
    public void setShowToolBarCheckMenuEntryDescriptor(CheckMenuEntryDescriptor showToolBarCheckMenuEntryDescriptor) {
        this.showToolBarCheckMenuEntryDescriptor = showToolBarCheckMenuEntryDescriptor;
    }
}
