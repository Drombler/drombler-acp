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
import org.drombler.acp.core.action.PositionableMenuItemAdapterFactory;
import org.drombler.acp.core.action.jaxb.ToolBarType;
import org.drombler.acp.core.action.spi.impl.ShowToolBarAction;
import org.drombler.acp.core.commons.util.OSGiResourceBundleUtils;
import org.osgi.framework.Bundle;
import org.softsmithy.lib.util.Positionable;

/**
 * A tool bar descriptor.
 *
 * @author puce
 */
public class ToolBarDescriptor implements Positionable {

    private String id;
    private String displayName;
    private int position;
    private boolean visible;
    private ToggleActionDescriptor<?> showToolBarActionDescriptor;
    private ToggleMenuEntryDescriptor showToolBarCheckMenuEntryDescriptor;

    /**
     * Creates a new instance of this class.
     */
    public ToolBarDescriptor() {
    }

    /**
     * Gets the tool bar id.
     *
     * @return the tool bar id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the tool bar id.
     *
     * @param id the tool bar id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name.
     *
     * @param displayName the display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the position.
     *
     * @return the position
     */
    @Override
    public int getPosition() {
        return position;
    }

    /**
     * Sets the position.
     *
     * @param position the position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Indicates if the tool bar should be visible by default.
     *
     * @return true, if the tool bar should be visible by default, else false
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets if the tool bar should be visible by default.
     *
     * @param visible true, if the tool bar should be visible by default, else false
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    /**
     * Gets the show tool bar {@link ToggleActionDescriptor}.
     *
     * @return the show tool bar ToggleActionDescriptor
     */
    public ToggleActionDescriptor<?> getShowToolBarActionDescriptor() {
        return showToolBarActionDescriptor;
    }

    /**
     * Sets the show tool bar {@link ToggleActionDescriptor}.
     *
     * @param showToolBarActionDescriptor the show tool bar ToggleActionDescriptor
     */
    public void setShowToolBarActionDescriptor(ToggleActionDescriptor<?> showToolBarActionDescriptor) {
        this.showToolBarActionDescriptor = showToolBarActionDescriptor;
    }

    /**
     * Gets the show tool bar {@link ToggleMenuEntryDescriptor}.
     *
     * @return the show tool bar ToggleMenuEntryDescriptor
     */
    public ToggleMenuEntryDescriptor getShowToolBarCheckMenuEntryDescriptor() {
        return showToolBarCheckMenuEntryDescriptor;
    }

    /**
     * Sets the show tool bar {@link ToggleMenuEntryDescriptor}.
     *
     * @param showToolBarCheckMenuEntryDescriptor the show tool bar ToggleMenuEntryDescriptor
     */
    public void setShowToolBarCheckMenuEntryDescriptor(ToggleMenuEntryDescriptor showToolBarCheckMenuEntryDescriptor) {
        this.showToolBarCheckMenuEntryDescriptor = showToolBarCheckMenuEntryDescriptor;
    }

    /**
     * Creates an instance of a {@link ToolBarDescriptor} from a {@link ToolBarType} unmarshalled from the application.xml.
     *
     * @param <T> the GUI-toolkit specific type of a tool bar
     * @param <B> the GUI-toolkit specific base type of a tool bar button.
     * @param toolBarType the unmarshalled ToolBarType
     * @param bundle the OSGi bundle of the application.xml
     * @param toolBarContainer the tool bar container which manages the tool bar
     * @return a ToolBarDescriptor
     */
    public static <T, B> ToolBarDescriptor createToolBarDescriptor(ToolBarType toolBarType, Bundle bundle, ToolBarContainer<T, B> toolBarContainer) {
        ToolBarDescriptor toolBarDescriptor = new ToolBarDescriptor();

        toolBarDescriptor.setId(StringUtils.stripToNull(toolBarType.getId()));
        toolBarDescriptor.setDisplayName(OSGiResourceBundleUtils.getPackageResourceStringPrefixed(toolBarType.getPackage(),
                toolBarType.getDisplayName(), bundle));
        toolBarDescriptor.setPosition(toolBarType.getPosition());
        toolBarDescriptor.setVisible(toolBarType.isVisible());
        ToggleActionDescriptor<ShowToolBarAction<T, B>> actionDescriptor = createShowToolBarActionDescriptor(
                toolBarDescriptor,
                toolBarContainer);
        toolBarDescriptor.setShowToolBarActionDescriptor(actionDescriptor);
        toolBarDescriptor.setShowToolBarCheckMenuEntryDescriptor(new ToggleMenuEntryDescriptor(actionDescriptor.getId(),
                "View/Toolbars", new PositionableMenuItemAdapterFactory<>(toolBarType.getPosition())));
        return toolBarDescriptor;
    }

    private static <T, B> ToggleActionDescriptor<ShowToolBarAction<T, B>> createShowToolBarActionDescriptor(
            ToolBarDescriptor toolBarDescriptor, ToolBarContainer<T, B> toolBarContainer) {
        ToggleActionDescriptor<ShowToolBarAction<T, B>> actionDescriptor = new ToggleActionDescriptor<>(
                (Class<ShowToolBarAction<T, B>>) (Class<?>) ShowToolBarAction.class);
        actionDescriptor.setId(ShowToolBarAction.class.getName() + "#" + toolBarDescriptor.getId()); // TODO: ok?
        actionDescriptor.setDisplayName(toolBarDescriptor.getDisplayName());
        actionDescriptor.setListener(new ShowToolBarAction<>(toolBarDescriptor.getId(), toolBarContainer));
        return actionDescriptor;
    }

}
