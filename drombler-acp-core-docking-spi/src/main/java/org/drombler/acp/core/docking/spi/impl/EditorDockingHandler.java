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
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.drombler.acp.core.commons.util.BundleUtils;
import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.acp.core.docking.spi.DockingDescriptorUtils;
import org.drombler.acp.core.docking.spi.EditorDockingDescriptor;
import org.drombler.acp.core.docking.spi.EditorDockingDescriptorRegistry;
import org.drombler.acp.startup.main.ApplicationExecutorProvider;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockablePreferences;
import org.drombler.commons.docking.DockingAreaDescriptor;
import org.drombler.commons.docking.context.DockingAreaContainer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softsmithy.lib.util.SetChangeEvent;
import org.softsmithy.lib.util.SetChangeListener;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "editorDockingDescriptor", referenceInterface = EditorDockingDescriptor.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    ,
    @Reference(name = "applicationExecutorProvider", referenceInterface = ApplicationExecutorProvider.class)})
public class EditorDockingHandler<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> extends AbstractDockableDockingHandler<D, DATA, E> {

    private static final Logger LOG = LoggerFactory.getLogger(EditorDockingHandler.class);

    private final List<EditorDockingDescriptor<? extends D>> unresolvedDockingDescriptors = new ArrayList<>();
    private final DockingAreaListener dockingAreaListener = new DockingAreaListener();

    @Reference
    private EditorDockingDescriptorRegistry<D> editorRegistry;
    private Executor applicationExecutor;


    protected void bindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = applicationExecutorProvider.getApplicationExecutor();
    }

    protected void unbindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = null;
    }

    protected void bindEditorDockingDescriptor(EditorDockingDescriptor<? extends D> dockingDescriptor) {
        resolveEditorDockingDescriptor(dockingDescriptor);
    }

    protected void unbindEditorDockingDescriptor(EditorDockingDescriptor<? extends D> dockingDescriptor) {
        unregisterDockable(dockingDescriptor.getId(), dockingDescriptor.getDockableClass(), dockingDescriptor.getContentType());
    }

    @Activate
    protected void activate(ComponentContext context) {
        getDockingAreaContainer().addDockingAreaSetChangeListener(dockingAreaListener);
        resolveUnresolvedDockables();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        getDockingAreaContainer().removeDockingAreaSetChangeListener(dockingAreaListener);
        unresolvedDockingDescriptors.clear();
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized()
                && getDockingAreaContainer().getDefaultEditorAreaId() != null
                && applicationExecutor != null;
    }

    @Override
    protected void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context) {
        dockingsType.getEditorDocking().forEach(dockingType -> {
            try {
                EditorDockingDescriptor<? extends D> dockingDescriptor
                        = (EditorDockingDescriptor<? extends D>) DockingDescriptorUtils.createEditorDockingDescriptor(dockingType, bundle);
                // TODO: register EditorDockingDescriptor as service? Omit resolveDockingDescriptor?
                resolveEditorDockingDescriptor(dockingDescriptor);
            } catch (ClassNotFoundException | RuntimeException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }

    @Override
    protected void unregisterDockingsType(DockingsType dockingsType, Bundle bundle) throws Exception {
        dockingsType.getEditorDocking().forEach(dockingType -> {
            try {
                // TODO: unregister EditorDockingDescriptor as service? Omit resolveDockingDescriptor?
                unregisterDockable(dockingType.getId(), (Class<? extends D>) BundleUtils.loadClass(bundle, dockingType.getDockableClass()), BundleUtils.loadClass(bundle, dockingType.getContentType()));
            } catch (ClassNotFoundException | RuntimeException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }

    private void resolveEditorDockingDescriptor(EditorDockingDescriptor<? extends D> dockingDescriptor) {
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
        unresolvedDockingDescriptorsCopy.forEach(this::resolveEditorDockingDescriptor);
    }

    private void unregisterDockable(String dockableId, Class<? extends D> dockableClass, Class<?> contentType) {
        if (isInitialized()) {
            unregisterDefaultDockablePreferences(dockableClass);
            editorRegistry.unregisterEditorDockingDescriptor(contentType);
            DockingAreaContainer<D, DATA, E> dockingAreaContainer = getDockingAreaContainer();
            applicationExecutor.execute(() -> {
                dockingAreaContainer.closeEditors(dockableClass);
            });
        } else {
            unresolvedDockingDescriptors.removeIf(unresolvedEntry -> unresolvedEntry.getId().equals(dockableId));
        }
    }

    private class DockingAreaListener implements SetChangeListener<DockingAreaDescriptor> {

        /**
         * This method gets called from the application thread!
         *
         * @param event
         */
        @Override
        public void elementAdded(SetChangeEvent<DockingAreaDescriptor> event) {
            resolveUnresolvedDockables();
        }

        /**
         * This method gets called from the application thread!
         *
         * @param event
         */
        @Override
        public void elementRemoved(SetChangeEvent<DockingAreaDescriptor> event) {
            // TODO: ???
        }

    }
}
