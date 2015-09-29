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

import org.apache.felix.scr.annotations.Reference;
import org.drombler.acp.core.docking.spi.DockingAreaContainerProvider;
import org.drombler.commons.docking.DockableEntry;

/**
 *
 * @author puce
 */
@Reference(name = "dockingAreaContainerProvider", referenceInterface = DockingAreaContainerProvider.class)
public abstract class AbstractDockingHandler<D, E extends DockableEntry<D>> {

    private DockingAreaContainerProvider<D, E> dockingAreaContainerProvider;

    protected void bindDockingAreaContainerProvider(
            DockingAreaContainerProvider<D, E> dockingAreaContainerProvider) {
        this.dockingAreaContainerProvider = dockingAreaContainerProvider;
    }

    protected void unbindDockingAreaContainerProvider(
            DockingAreaContainerProvider<D, E> dockingAreaContainerProvider) {
        this.dockingAreaContainerProvider = null;
    }

    /**
     * @return the dockingAreaContainer
     */
    protected DockingAreaContainerProvider<D, E> getDockingAreaContainerProvider() {
        return dockingAreaContainerProvider;
    }

    protected boolean isInitialized() {
        return dockingAreaContainerProvider != null;
    }
}
