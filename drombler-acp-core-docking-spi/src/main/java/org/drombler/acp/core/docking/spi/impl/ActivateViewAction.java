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
package org.drombler.acp.core.docking.spi.impl;

import org.drombler.acp.core.docking.spi.Dockables;
import org.drombler.commons.action.AbstractActionListener;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;

/**
 *
 * @author puce
 */
public class ActivateViewAction<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> extends AbstractActionListener<Object> {

    private final E viewEntry;

    public ActivateViewAction(E viewEntry) {
        this.viewEntry = viewEntry;
    }

    @Override
    public void onAction(Object event) {
        Dockables.openView(viewEntry);
//        dockable.requestActive();
    }
}
