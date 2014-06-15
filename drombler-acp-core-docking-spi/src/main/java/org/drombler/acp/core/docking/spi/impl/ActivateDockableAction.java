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

import org.drombler.commons.action.AbstractActionListener;
import org.drombler.acp.core.docking.spi.Dockables;
import org.drombler.commons.client.docking.Dockable;

/**
 *
 * @author puce
 */
public class ActivateDockableAction extends AbstractActionListener<Object> {

    private final Dockable dockable;

    public ActivateDockableAction(Dockable dockable) {
        this.dockable = dockable;
    }

    @Override
    public void onAction(Object event) {
        Dockables.open(dockable);
        dockable.requestActive();
    }
}
