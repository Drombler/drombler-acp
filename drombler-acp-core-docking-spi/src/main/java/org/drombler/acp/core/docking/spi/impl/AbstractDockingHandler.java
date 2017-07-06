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
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockablePreferences;
import org.drombler.commons.docking.context.DockingAreaContainer;

/**
 *
 * @author puce
 * @param <D>
 * @param <DATA>
 * @param <E>
 */
@Reference(name = "dockingAreaContainerProvider", referenceInterface = DockingAreaContainerProvider.class)
public abstract class AbstractDockingHandler<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> {

    private DockingAreaContainerProvider<D, DATA, E> dockingAreaContainerProvider;

    protected void bindDockingAreaContainerProvider(DockingAreaContainerProvider<D, DATA, E> dockingAreaContainerProvider) {
        this.dockingAreaContainerProvider = dockingAreaContainerProvider;
    }

    protected void unbindDockingAreaContainerProvider(DockingAreaContainerProvider<D, DATA, E> dockingAreaContainerProvider) {
        this.dockingAreaContainerProvider = null;
    }

    protected void registerDefaultDockablePreferences(Class<?> dockableClass, DockablePreferences dockablePreferences) {
        getDockingAreaContainer().registerDefaultDockablePreferences(dockableClass, dockablePreferences);
    }

    protected DockablePreferences unregisterDefaultDockablePreferences(Class<?> dockableClass) {
        if (isInitialized()) {
            return getDockingAreaContainer().unregisterDefaultDockablePreferences(dockableClass);
        } else {
            return null;
        }
    }

    protected DockingAreaContainer<D, DATA, E> getDockingAreaContainer() {
        return dockingAreaContainerProvider.getDockingAreaContainer();
    }

    protected boolean isInitialized() {
        return dockingAreaContainerProvider != null;
    }
}
