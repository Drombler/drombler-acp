/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.spi.impl;

import org.apache.felix.scr.annotations.Reference;
import org.richclientplatform.core.docking.spi.DockingAreaContainer;
import org.richclientplatform.core.docking.spi.DockingAreaContainerProvider;

/**
 *
 * @author puce
 */
@Reference(name = "dockingAreaContainerProvider", referenceInterface = DockingAreaContainerProvider.class)
public abstract class AbstractDockingHandler<A, D> {

    private DockingAreaContainer<A, D> dockingAreaContainer;

    protected void bindDockingAreaContainerProvider(DockingAreaContainerProvider<A, D> dockingAreaContainerProvider) {
        this.dockingAreaContainer = dockingAreaContainerProvider.getDockingAreaContainer();
    }

    protected void unbindDockingAreaContainerProvider(DockingAreaContainerProvider<A, D> dockingAreaContainerProvider) {
        this.dockingAreaContainer = null;
    }

    /**
     * @return the dockingAreaContainer
     */
    protected DockingAreaContainer<A, D> getDockingAreaContainer() {
        return dockingAreaContainer;
    }

    protected boolean isInitialized() {
        return dockingAreaContainer != null;
    }
}
