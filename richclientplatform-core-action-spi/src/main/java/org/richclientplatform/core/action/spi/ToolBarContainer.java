/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.richclientplatform.core.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
public interface ToolBarContainer<T, B> {

    void addToolBar(String toolBarId, PositionableAdapter<T> toolBar);

    void addToolBarButton(String toolBarId, PositionableAdapter<? extends B> toolBarButton);

    boolean isToolBarVisible(String toolBarId);

    void setToolBarVisible(String toolBarId, boolean visible);

    void addToolBarContainerListener(ToolBarContainerListener<T, B> containerListener);

    void removeToolBarContainerListener(ToolBarContainerListener<T, B> containerListener);

    void addToolBarContainerListener(String toolBarId, ToolBarContainerListener<T, B> containerListener);

    void removeToolBarContainerListener(String toolBarId, ToolBarContainerListener<T, B> containerListener);
}
