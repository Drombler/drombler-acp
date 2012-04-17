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

    void addToolBarButton(String toolBarId, PositionableAdapter<B> toolBarButton);
}
