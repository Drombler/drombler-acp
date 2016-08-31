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
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.acp.core.docking.spi.DockingDescriptorUtils;
import org.drombler.acp.core.docking.spi.EditorDockingDescriptor;
import org.drombler.acp.core.docking.spi.EditorDockingDescriptorRegistry;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockablePreferences;
import org.drombler.commons.docking.context.DockingAreaContainerActiveDockableChangedEvent;
import org.drombler.commons.docking.context.DockingAreaContainerDockableEvent;
import org.drombler.commons.docking.context.DockingAreaContainerDockingAreaEvent;
import org.drombler.commons.docking.context.DockingAreaContainerListener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "editorDockingDescriptor", referenceInterface = EditorDockingDescriptor.class,
        cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class EditorDockingHandler<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> extends AbstractDockableDockingHandler<D, DATA, E> {

    private static final Logger LOG = LoggerFactory.getLogger(EditorDockingHandler.class);

    private final List<EditorDockingDescriptor<? extends D>> unresolvedDockingDescriptors = new ArrayList<>();
    private final DockingAreaListener dockingAreaListener = new DockingAreaListener();

    @Reference
    private EditorDockingDescriptorRegistry<D> editorRegistry;


    protected void bindEditorDockingDescriptor(EditorDockingDescriptor<? extends D> dockingDescriptor) {
        resolveDockingDescriptor(dockingDescriptor);
    }

    protected void unbindEditorDockingDescriptor(EditorDockingDescriptor<?> dockingDescriptor) {
    }

    @Activate
    protected void activate(ComponentContext context) {
        getDockingAreaContainer().addDockingAreaContainerListener(dockingAreaListener);
        resolveUnresolvedDockables();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        getDockingAreaContainer().removeDockingAreaContainerListener(dockingAreaListener);
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized()
                && getDockingAreaContainer().getDefaultEditorAreaId() != null;
    }

    @Override
    protected void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context) {
        dockingsType.getEditorDocking().forEach(dockingType -> {
            try {
                EditorDockingDescriptor<? extends D> dockingDescriptor
                        = (EditorDockingDescriptor<? extends D>) DockingDescriptorUtils.createEditorDockingDescriptor(dockingType, bundle);
                // TODO: register EditorDockingDescriptor as service? Omit resolveDockingDescriptor?
                resolveDockingDescriptor(dockingDescriptor);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }

    private void resolveDockingDescriptor(EditorDockingDescriptor<? extends D> dockingDescriptor) {
        if (isInitialized()) {
            editorRegistry.registerEditorDockingDescriptor(dockingDescriptor.getContentType(), dockingDescriptor);
            String defaultEditorAreaId = getDockingAreaContainer().getDefaultEditorAreaId();
            DockablePreferences dockablePreferences = new DockablePreferences(defaultEditorAreaId, 0);
            registerDefaultDockablePreferences(dockingDescriptor.getDockableClass(), dockablePreferences);
        } else {
            unresolvedDockingDescriptors.add(dockingDescriptor);
        }
    }

    private void resolveUnresolvedDockables() {
        List<EditorDockingDescriptor<? extends D>> unresolvedDockingDescriptorsCopy = new ArrayList<>(unresolvedDockingDescriptors);
        unresolvedDockingDescriptors.clear();
        unresolvedDockingDescriptorsCopy.forEach(this::resolveDockingDescriptor);
    }

    private class DockingAreaListener implements DockingAreaContainerListener<D, DATA, E> {

        /**
         * This method gets called from the application thread!
         *
         * @param event
         */
        @Override
        public void dockingAreaAdded(DockingAreaContainerDockingAreaEvent<D, DATA, E> event) {
            resolveUnresolvedDockables();
        }

        /**
         * This method gets called from the application thread!
         *
         * @param event
         */
        @Override
        public void dockingAreaRemoved(DockingAreaContainerDockingAreaEvent<D, DATA, E> event) {
            // TODO: ???
        }

        @Override
        public void dockableAdded(DockingAreaContainerDockableEvent<D, DATA, E> event) {
            // do nothing
        }

        @Override
        public void dockableRemoved(DockingAreaContainerDockableEvent<D, DATA, E> event) {
            // do nothing
        }

        @Override
        public void activeDockableChanged(DockingAreaContainerActiveDockableChangedEvent<D, DATA, E> event) {
            // do nothing
        }

    }
}
