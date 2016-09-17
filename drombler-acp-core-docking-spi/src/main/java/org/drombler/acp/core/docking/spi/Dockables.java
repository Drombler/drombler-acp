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
 * Copyright 2014 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.docking.spi;

import org.drombler.acp.core.data.spi.DataHandlerDescriptorRegistryProvider;
import org.drombler.acp.startup.main.ApplicationExecutorProvider;
import org.drombler.commons.data.AbstractDataHandlerDescriptor;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Move to API? But DockingAreaContainerProvider is a SPI interface...
 *
 * @author puce
 */
public final class Dockables {

    private static final Logger LOG = LoggerFactory.getLogger(Dockables.class);

    private Dockables() {
    }

    public static <D, DATA extends DockableData, E extends DockableEntry<D, DATA>> void openView(D dockable) {
        // TODO: cache ServiceReference? or even DockingAreaContainerProvider?
        // TODO: check if the code is safe, if the services disappear
        BundleContext bundleContext = FrameworkUtil.getBundle(Dockables.class).getBundleContext();

        @SuppressWarnings("rawtypes")
        ServiceReference<DockingAreaContainerProvider> dockingAreaContainerProviderServiceReference
                = bundleContext.getServiceReference(DockingAreaContainerProvider.class);
        @SuppressWarnings("unchecked")
        DockingAreaContainerProvider<D, DATA, E> dockingAreaContainerProvider
                = bundleContext.getService(dockingAreaContainerProviderServiceReference);

        ServiceReference<ApplicationExecutorProvider> applicationExecutorProviderServiceReference
                = bundleContext.getServiceReference(ApplicationExecutorProvider.class);
        ApplicationExecutorProvider applicationExecutorProvider
                = bundleContext.getService(applicationExecutorProviderServiceReference);

        applicationExecutorProvider.getApplicationExecutor().execute(() -> dockingAreaContainerProvider.getDockingAreaContainer().openView(dockable, true));

        bundleContext.ungetService(applicationExecutorProviderServiceReference);
        bundleContext.ungetService(dockingAreaContainerProviderServiceReference);
    }

    public static <D, DATA extends DockableData, E extends DockableEntry<D, DATA>> void openEditorForContent(Object content) {
        // TODO: cache ServiceReference?
        // TODO: check if the code is safe, if the services disappear
        BundleContext bundleContext = FrameworkUtil.getBundle(Dockables.class).getBundleContext();

        @SuppressWarnings("rawtypes")
        ServiceReference<DockingAreaContainerProvider> dockingAreaContainerProviderServiceReference
                = bundleContext.getServiceReference(DockingAreaContainerProvider.class);
        @SuppressWarnings("unchecked")
        DockingAreaContainerProvider<D, DATA, E> dockingAreaContainerProvider
                = bundleContext.getService(dockingAreaContainerProviderServiceReference);

        ServiceReference<DataHandlerDescriptorRegistryProvider> dataHandlerDescriptorRegistryProviderServiceReference
                = bundleContext.getServiceReference(DataHandlerDescriptorRegistryProvider.class);
        DataHandlerDescriptorRegistryProvider dataHandlerDescriptorRegistryProvider
                = bundleContext.getService(dataHandlerDescriptorRegistryProviderServiceReference);

        @SuppressWarnings("rawtypes")
        ServiceReference<EditorDockingDescriptorRegistry> editorRegistryServiceReference
                = bundleContext.getServiceReference(EditorDockingDescriptorRegistry.class);
        @SuppressWarnings("unchecked")
        EditorDockingDescriptorRegistry<D> editorDockingDescriptorRegistry
                = bundleContext.getService(editorRegistryServiceReference);

        ServiceReference<ApplicationExecutorProvider> applicationExecutorProviderServiceReference
                = bundleContext.getServiceReference(ApplicationExecutorProvider.class);
        ApplicationExecutorProvider applicationExecutorProvider
                = bundleContext.getService(applicationExecutorProviderServiceReference);

        EditorDockingDescriptor<? extends D> editorDockingDescriptor = editorDockingDescriptorRegistry.getEditorDockingDescriptor(content.getClass());
        AbstractDataHandlerDescriptor<?> dataHandlerDescriptor = dataHandlerDescriptorRegistryProvider.getDataHandlerDescriptorRegistry().getDataHandlerDescriptor(content);

        if (editorDockingDescriptor != null && dataHandlerDescriptor != null) {
            applicationExecutorProvider.getApplicationExecutor().execute(() -> dockingAreaContainerProvider.getDockingAreaContainer().openEditorForContent(content, editorDockingDescriptor.
                    getDockableClass(),
                    dataHandlerDescriptor.getIcon(), dataHandlerDescriptor.getResourceLoader()));
        } else {
            LOG.error("No editor or no data handler registered for the provided content!"); // TODO: better message
        }

        bundleContext.ungetService(applicationExecutorProviderServiceReference);
        bundleContext.ungetService(editorRegistryServiceReference);
        bundleContext.ungetService(dataHandlerDescriptorRegistryProviderServiceReference);
        bundleContext.ungetService(dockingAreaContainerProviderServiceReference);
    }

}
