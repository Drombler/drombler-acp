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

import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.acp.core.data.spi.DataHandlerDescriptorRegistryProvider;
import org.drombler.commons.data.AbstractDataHandlerDescriptor;
import org.drombler.commons.data.DataHandlerDescriptorRegistry;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.context.DockingAreaContainer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softsmithy.lib.util.UniqueKeyProvider;

/**
 * A utility class for Dockables.
 *
 * @author puce
 */
// TODO: Move to API? But DockingAreaContainerProvider is a SPI interface...
public final class Dockables {

    private static final Logger LOG = LoggerFactory.getLogger(Dockables.class);

    private Dockables() {
    }

    /**
     * Opens a View.
     *
     * @param <D> the Dockable type
     * @param <DATA> the Dockable data type
     * @param <E> the Dockable entry type
     * @param viewEntry the view entry to open
     */
    public static <D, DATA extends DockableData, E extends DockableEntry<D, DATA>> void openView(E viewEntry) {
        // TODO: cache ServiceReference? or even DockingAreaContainerProvider?
        // TODO: check if the code is safe, if the services disappear
        BundleContext bundleContext = FrameworkUtil.getBundle(Dockables.class).getBundleContext();

        @SuppressWarnings("rawtypes")
        ServiceReference<DockingAreaContainerProvider> dockingAreaContainerProviderServiceReference
                = bundleContext.getServiceReference(DockingAreaContainerProvider.class);
        @SuppressWarnings("unchecked")
        DockingAreaContainerProvider<D, DATA, E> dockingAreaContainerProvider
                = bundleContext.getService(dockingAreaContainerProviderServiceReference);

        ServiceReference<ApplicationThreadExecutorProvider> applicationThreadExecutorProviderServiceReference
                = bundleContext.getServiceReference(ApplicationThreadExecutorProvider.class);
        ApplicationThreadExecutorProvider applicationThreadExecutorProvider
                = bundleContext.getService(applicationThreadExecutorProviderServiceReference);

        applicationThreadExecutorProvider.getApplicationThreadExecutor().execute(() -> dockingAreaContainerProvider.getDockingAreaContainer().addDockable(viewEntry, true));

        bundleContext.ungetService(applicationThreadExecutorProviderServiceReference);
        bundleContext.ungetService(dockingAreaContainerProviderServiceReference);
    }

    /**
     * Opens an Editor for the specified content.
     *
     * It looks in the {@link EditorDockingDescriptorRegistry} if there is any registered {@link EditorDockingDescriptor} for the content type. If one is registered it looks in the
     * {@link DataHandlerDescriptorRegistry} if there is any registerd Data Handler descriptor. If there is one it tries to open an editor by calling
     * {@link DockingAreaContainer#openEditorForContent(java.lang.Object, java.lang.Class, java.lang.String, org.softsmithy.lib.util.ResourceLoader)}.
     *
     * @param <D> the Dockable type
     * @param <DATA> the Dockable data type
     * @param <E> the Dockable entry type
     * @param content the content to open in an Editor
     */
    public static <D, DATA extends DockableData, E extends DockableEntry<D, DATA>> void openEditorForContent(UniqueKeyProvider<?> content) {
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

        ServiceReference<ApplicationThreadExecutorProvider> applicationThreadExecutorProviderServiceReference
                = bundleContext.getServiceReference(ApplicationThreadExecutorProvider.class);
        ApplicationThreadExecutorProvider applicationThreadExecutorProvider
                = bundleContext.getService(applicationThreadExecutorProviderServiceReference);

        EditorDockingDescriptor<? extends D> editorDockingDescriptor = editorDockingDescriptorRegistry.getEditorDockingDescriptor(content.getClass());
        AbstractDataHandlerDescriptor<?> dataHandlerDescriptor = dataHandlerDescriptorRegistryProvider.getDataHandlerDescriptorRegistry().getDataHandlerDescriptor(content);

        if (editorDockingDescriptor != null && dataHandlerDescriptor != null) {
            applicationThreadExecutorProvider.getApplicationThreadExecutor().execute(() -> dockingAreaContainerProvider.getDockingAreaContainer().openEditorForContent(content, editorDockingDescriptor.
                    getDockableClass(),
                    dataHandlerDescriptor.getIcon(), dataHandlerDescriptor.getResourceLoader()));
        } else {
            LOG.error("No editor or no data handler registered for the provided content!"); // TODO: better message
        }

        bundleContext.ungetService(applicationThreadExecutorProviderServiceReference);
        bundleContext.ungetService(editorRegistryServiceReference);
        bundleContext.ungetService(dataHandlerDescriptorRegistryProviderServiceReference);
        bundleContext.ungetService(dockingAreaContainerProviderServiceReference);
    }

}
