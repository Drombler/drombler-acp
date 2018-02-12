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

import java.util.EventObject;
import org.softsmithy.lib.util.PositionableAdapter;

/**
 * An abstract base class for {@link ToolBarContainer} events.
 *
 * @param <ToolBar> the GUI-toolkit specific type of a tool bar
 * @param <ToolBarButton> the GUI-toolkit specific base type of a tool bar button.
 * @param <T> the entry type of this event - ToolBar or ToolBarButton
 * @see ToolBarContainerListener
 */
public abstract class AbstractToolBarContainerEvent<ToolBar, ToolBarButton, T> extends EventObject {

    private static final long serialVersionUID = 1674571570888510979L;

    private final String toolBarId;
    private final PositionableAdapter<? extends T> entry;

    /**
     * Creates a new instance of this class.
     *
     * @param source the source container
     * @param toolBarId the tool bar id
     * @param entry the tool bar container entry
     */
    public AbstractToolBarContainerEvent(ToolBarContainer<ToolBar, ToolBarButton> source, String toolBarId, PositionableAdapter<? extends T> entry) {
        super(source);
        this.toolBarId = toolBarId;
        this.entry = entry;
    }

    /**
     * Gets the tool bar id.
     *
     * @return the tool bar id
     */
    public String getToolBarId() {
        return toolBarId;
    }

    /**
     * Gets the tool bar container entry.
     *
     * @return the tool bar container entry
     */
    public PositionableAdapter<? extends T> getEntry() {
        return entry;
    }
}
