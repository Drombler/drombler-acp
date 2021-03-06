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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.acp.core.docking.jaxb.DockingAreaType;
import org.drombler.acp.core.docking.jaxb.DockingAreasType;
import org.drombler.acp.core.docking.spi.DockingAreaDescriptorUtils;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockingAreaDescriptor;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
public class DockingAreaHandler<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> extends AbstractDockingHandler<D, DATA, E> {

    private static final Logger LOG = LoggerFactory.getLogger(DockingAreaHandler.class);

    private Executor applicationExecutor;
    private final List<DockingAreaDescriptor> unresolvedDockingAreaDescriptors = new ArrayList<>();

    @Reference
    protected void bindApplicationThreadExecutorProvider(ApplicationThreadExecutorProvider applicationThreadExecutorProvider) {
        applicationExecutor = applicationThreadExecutorProvider.getApplicationThreadExecutor();
    }

    protected void unbindApplicationThreadExecutorProvider(ApplicationThreadExecutorProvider applicationThreadExecutorProvider) {
        applicationExecutor = null;
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindDockingAreasType(DockingAreasType dockingAreasType) {
        for (DockingAreaType dockingArea : dockingAreasType.getDockingArea()) {
            DockingAreaDescriptor dockingAreaDescriptor = DockingAreaDescriptorUtils.createDockingAreaDescriptor(
                    dockingArea);
            // TODO: register DockingAreaDescriptor as service? Omit resolveDockingArea?
            resolveDockingArea(dockingAreaDescriptor);
        }
    }

    protected void unbindDockingAreasType(DockingAreasType dockingAreasType) {
        // TODO
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindDockingAreaDescriptor(DockingAreaDescriptor dockingAreaDescriptor) {
        resolveDockingArea(dockingAreaDescriptor);
    }

    protected void unbindDockingAreaDescriptor(DockingAreaDescriptor dockingAreaDescriptor) {
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedDockingAreas();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && applicationExecutor != null;
    }

    private void resolveDockingArea(final DockingAreaDescriptor dockingAreaDescriptor) {
        if (isInitialized()) {
            LOG.info("Adding docking area ({}): '{}'", dockingAreaDescriptor.getKind(), dockingAreaDescriptor.getId());
            Runnable runnable = ()
                    -> getDockingAreaContainer().addDockingArea(dockingAreaDescriptor);
            applicationExecutor.execute(runnable);
        } else {
            unresolvedDockingAreaDescriptors.add(dockingAreaDescriptor);
        }
    }

    private void resolveUnresolvedDockingAreas() {
        for (DockingAreaDescriptor unresolvedDockingAreaDescriptor : unresolvedDockingAreaDescriptors) {
            resolveDockingArea(unresolvedDockingAreaDescriptor);
        }
    }
}
