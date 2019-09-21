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

import org.softsmithy.lib.util.PositionableAdapter;

/**
 * A tool bar container, which manages tool bars and tool bar buttons.<br>
 * <br>
 * A tool bar container can contain multiple tool bars each identified by an unique id.
 * <br>
 * The tool bars can be visible or hidden.<br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @param <T> the GUI-toolkit specific type of a tool bar
 * @param <B> the GUI-toolkit specific base type of a tool bar button.
 */
public interface ToolBarContainer<T, B> {

    /**
     * Adds a tool bar to this container.
     *
     * @param toolBarId the id of the tool bar
     * @param toolBar the tool bar with the preferred position
     */
    void addToolBar(String toolBarId, PositionableAdapter<T> toolBar);

    /**
     * Removes the tool bar with the specified id from this container.
     *
     * @param toolBarId the tool bar id
     * @return the removed toolBar or null if not found
     */
    PositionableAdapter<T> removeToolBar(String toolBarId);

    /**
     * Adds a tool bar button to the tool bar with the specified id.
     *
     * @param toolBarId the id of the tool bar the provided button should be added to
     * @param toolBarButton tbe tool bar button with the preferred position
     */
    void addToolBarButton(String toolBarId, PositionableAdapter<? extends B> toolBarButton);

    /**
     * Removes a tool bar button from the tool bar with the specified id.
     *
     * @param toolBarId the id of the tool bar the provided button should be removed from
     * @param toolBarButton tbe tool bar button
     * @return the removed tool bar button or null if not found
     */
    PositionableAdapter<? extends B> removeToolBarButton(String toolBarId, B toolBarButton);

    /**
     * Indicates if this container contains a tool bar with the specified id
     *
     * @param toolBarId the tool bar id
     * @return true, if this container contains a tool bar with the specified id, else false
     */
    boolean containsToolBar(String toolBarId);

    /**
     * Indicates if the tool bar with the specified id is visible
     *
     * @param toolBarId the tool bar id
     * @return true, if the tool bar is visible, else false
     */
    boolean isToolBarVisible(String toolBarId);

    /**
     * Sets the visible property of the tool bar with the specified id.
     *
     * @param toolBarId the tool bar id
     * @param visible the flag
     */
    void setToolBarVisible(String toolBarId, boolean visible);

    /**
     * Adds a tool bar container listener.
     *
     * @param containerListener a tool bar container listener
     */
    void addToolBarContainerListener(ToolBarContainerListener<T, B> containerListener);

    /**
     * Removes a tool bar container listener.
     *
     * @param containerListener a tool bar container listener
     */
    void removeToolBarContainerListener(ToolBarContainerListener<T, B> containerListener);

    /**
     * Adds a tool bar container listener only for the specified tool bar id.
     *
     * @param toolBarId the tool bar id
     * @param containerListener a tool bar container listener
     */
    void addToolBarContainerListener(String toolBarId, ToolBarContainerListener<T, B> containerListener);

    /**
     * Removes a tool bar container listener only for the specified tool bar id.
     *
     * @param toolBarId the tool bar id
     * @param containerListener a tool bar container listener
     */
    void removeToolBarContainerListener(String toolBarId, ToolBarContainerListener<T, B> containerListener);
}
