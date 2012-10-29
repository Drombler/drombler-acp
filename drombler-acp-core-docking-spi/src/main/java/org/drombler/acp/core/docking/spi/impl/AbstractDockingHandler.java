/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking.spi.impl;

import org.apache.felix.scr.annotations.Reference;
import org.drombler.acp.core.docking.spi.DockingAreaContainerProvider;

/**
 *
 * @author puce
 */
@Reference(name = "dockingAreaContainerProvider", referenceInterface = DockingAreaContainerProvider.class)
public abstract class AbstractDockingHandler<A, D> {

    private DockingAreaContainerProvider<A, D> dockingAreaContainerProvider;

    protected void bindDockingAreaContainerProvider(DockingAreaContainerProvider<A, D> dockingAreaContainerProvider) {
        this.dockingAreaContainerProvider = dockingAreaContainerProvider;
    }

    protected void unbindDockingAreaContainerProvider(DockingAreaContainerProvider<A, D> dockingAreaContainerProvider) {
        this.dockingAreaContainerProvider = null;
    }

    /**
     * @return the dockingAreaContainer
     */
    protected DockingAreaContainerProvider<A, D> getDockingAreaContainerProvider() {
        return dockingAreaContainerProvider;
    }

    protected boolean isInitialized() {
        return dockingAreaContainerProvider != null;
    }
}
