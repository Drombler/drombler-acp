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

import org.drombler.acp.core.commons.util.PositionableAdapter;

/**
 *
 * @author puce
 */
public interface ToolBarContainer<T, B> {

    void addToolBar(String toolBarId, PositionableAdapter<T> toolBar);

    void addToolBarButton(String toolBarId, PositionableAdapter<? extends B> toolBarButton);

    boolean containsToolBar(String toolBarId);

    boolean isToolBarVisible(String toolBarId);

    void setToolBarVisible(String toolBarId, boolean visible);

    void addToolBarContainerListener(ToolBarContainerListener<T, B> containerListener);

    void removeToolBarContainerListener(ToolBarContainerListener<T, B> containerListener);

    void addToolBarContainerListener(String toolBarId, ToolBarContainerListener<T, B> containerListener);

    void removeToolBarContainerListener(String toolBarId, ToolBarContainerListener<T, B> containerListener);
}
