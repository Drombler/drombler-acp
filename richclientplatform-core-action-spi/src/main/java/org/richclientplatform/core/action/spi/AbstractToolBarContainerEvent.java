/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import java.util.EventObject;
import org.richclientplatform.core.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
public abstract class AbstractToolBarContainerEvent<ToolBar, ToolBarButton, T> extends EventObject {

    private final PositionableAdapter<? extends T> entry;

    public AbstractToolBarContainerEvent(ToolBarContainer<ToolBar, ToolBarButton> source, PositionableAdapter<? extends T> entry) {
        super(source);
        this.entry = entry;
    }

    /**
     * @return the menuItem
     */
    public PositionableAdapter<? extends T> getEntry() {
        return entry;
    }
}
