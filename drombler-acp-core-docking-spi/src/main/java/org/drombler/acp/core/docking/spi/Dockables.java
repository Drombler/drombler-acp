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

import java.lang.reflect.InvocationTargetException;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptor;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistry;
import org.drombler.commons.context.ActiveContextProvider;
import org.drombler.commons.context.ApplicationContextProvider;
import org.drombler.commons.context.ContextInjector;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableDataManager;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockableKind;
import org.drombler.commons.docking.DockingInjector;
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

    public static <D, E extends DockableEntry<D>> void openView(D dockable) {
        // TODO: cache ServiceReference? or even DockingAreaContainerProvider?
        // TODO: check if the code is safe, if the services disappear
        BundleContext bundleContext = FrameworkUtil.getBundle(Dockables.class).getBundleContext();

        // TODO: inject DockablePreferencesManagerProvider into DockableEntryFactory (possibly an abstract base implementation)?
        @SuppressWarnings("rawtypes")
        ServiceReference<DockablePreferencesManagerProvider> dockablePreferencesManagerProviderServiceReference
                = bundleContext.getServiceReference(DockablePreferencesManagerProvider.class);
        @SuppressWarnings("unchecked")
        DockablePreferencesManagerProvider<D> dockablePreferencesManagerProvider
                = bundleContext.getService(dockablePreferencesManagerProviderServiceReference);

        @SuppressWarnings("rawtypes")
        ServiceReference<DockingAreaContainerProvider> dockingAreaContainerProviderServiceReference
                = bundleContext.getServiceReference(DockingAreaContainerProvider.class);
        @SuppressWarnings("unchecked")
        DockingAreaContainerProvider<D, E> dockingAreaContainerProvider
                = bundleContext.getService(dockingAreaContainerProviderServiceReference);

        @SuppressWarnings("rawtypes")
        ServiceReference<DockableEntryFactory> dockableEntryFactoryServiceReference
                = bundleContext.getServiceReference(DockableEntryFactory.class);
        @SuppressWarnings("unchecked")
        DockableEntryFactory<D, E> dockableEntryFactory
                = bundleContext.getService(dockableEntryFactoryServiceReference);

        dockingAreaContainerProvider.getDockingAreaContainer().addDockable(dockableEntryFactory.createDockableEntry(
                dockable, DockableKind.VIEW,
                dockablePreferencesManagerProvider.getDockablePreferencesManager().getDockablePreferences(dockable)));

        bundleContext.ungetService(dockingAreaContainerProviderServiceReference);
        bundleContext.ungetService(dockablePreferencesManagerProviderServiceReference);
        bundleContext.ungetService(dockableEntryFactoryServiceReference);
    }

    public static <D, E extends DockableEntry<D>, DATA extends DockableData> void openEditorForContent(Object content) {
        // TODO: cache ServiceReference? or even DockingAreaContainerProvider?
        // TODO: check if the code is safe, if the services disappear
        BundleContext bundleContext = FrameworkUtil.getBundle(Dockables.class).getBundleContext();

        // TODO: inject DockablePreferencesManagerProvider into DockableEntryFactory (possibly an abstract base implementation)?
        @SuppressWarnings("rawtypes")
        ServiceReference<DockablePreferencesManagerProvider> dockablePreferencesManagerProviderServiceReference
                = bundleContext.getServiceReference(DockablePreferencesManagerProvider.class);
        @SuppressWarnings("unchecked")
        DockablePreferencesManagerProvider<D> dockablePreferencesManagerProvider
                = bundleContext.getService(dockablePreferencesManagerProviderServiceReference);

        @SuppressWarnings("rawtypes")
        ServiceReference<DocumentHandlerDescriptorRegistry> documentHandlerDescriptorRegistryServiceReference
                = bundleContext.getServiceReference(DocumentHandlerDescriptorRegistry.class);
        @SuppressWarnings("unchecked")
        DocumentHandlerDescriptorRegistry documentHandlerDescriptorRegistry
                = bundleContext.getService(documentHandlerDescriptorRegistryServiceReference);

        @SuppressWarnings("rawtypes")
        ServiceReference<DockableDataFactory> dockableDataFactoryServiceReference
                = bundleContext.getServiceReference(DockableDataFactory.class);
        @SuppressWarnings("unchecked")
        DockableDataFactory<DATA> dockableDataFactory
                = bundleContext.getService(dockableDataFactoryServiceReference);

        @SuppressWarnings("rawtypes")
        ServiceReference<DockableDataManagerProvider> dockableDataManagerProviderServiceReference
                = bundleContext.getServiceReference(DockableDataManagerProvider.class);
        @SuppressWarnings("unchecked")
        DockableDataManagerProvider<D, DATA> dockableDataManagerProvider
                = bundleContext.getService(dockableDataManagerProviderServiceReference);

        @SuppressWarnings("rawtypes")
        ServiceReference<DockingAreaContainerProvider> dockingAreaContainerProviderServiceReference
                = bundleContext.getServiceReference(DockingAreaContainerProvider.class);
        @SuppressWarnings("unchecked")
        DockingAreaContainerProvider<D, E> dockingAreaContainerProvider
                = bundleContext.getService(dockingAreaContainerProviderServiceReference);

        @SuppressWarnings("rawtypes")
        ServiceReference<DockableEntryFactory> dockableEntryFactoryServiceReference
                = bundleContext.getServiceReference(DockableEntryFactory.class);
        @SuppressWarnings("unchecked")
        DockableEntryFactory<D, E> dockableEntryFactory
                = bundleContext.getService(dockableEntryFactoryServiceReference);

        @SuppressWarnings("rawtypes")
        ServiceReference<EditorDockingDescriptorRegistry> editorRegistryServiceReference
                = bundleContext.getServiceReference(EditorDockingDescriptorRegistry.class);
        @SuppressWarnings("unchecked")
        EditorDockingDescriptorRegistry<D> editorDockingDescriptorRegistry
                = bundleContext.getService(editorRegistryServiceReference);

        EditorDockingDescriptor<? extends D> editorDockingDescriptor = editorDockingDescriptorRegistry.getEditorDockingDescriptor(content.getClass());
        DocumentHandlerDescriptor<?> documentHandlerDescriptor = documentHandlerDescriptorRegistry.getDocumentHandlerDescriptor(content);
        if (editorDockingDescriptor != null && documentHandlerDescriptor != null) {
            try {
                D editor = editorDockingDescriptor.createEditor(content);
                DATA dockableData = dockableDataFactory.createDockableData(documentHandlerDescriptor.getIcon(), documentHandlerDescriptor.getResourceLoader());
                dockableDataManagerProvider.getDockableDataManager().registerDockableData(editor, dockableData);

                inject(editor);

                dockingAreaContainerProvider.getDockingAreaContainer().addDockable(
                        dockableEntryFactory.createDockableEntry(editor, DockableKind.EDITOR,
                                dockablePreferencesManagerProvider.getDockablePreferencesManager().getDockablePreferences(editor)));
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {

        }
        bundleContext.ungetService(editorRegistryServiceReference);
        bundleContext.ungetService(dockingAreaContainerProviderServiceReference);
        bundleContext.ungetService(dockableDataManagerProviderServiceReference);
        bundleContext.ungetService(dockableDataFactoryServiceReference);
        bundleContext.ungetService(documentHandlerDescriptorRegistryServiceReference);
        bundleContext.ungetService(dockablePreferencesManagerProviderServiceReference);
        bundleContext.ungetService(dockableEntryFactoryServiceReference);
    }

    public static <D> void inject(D dockable) {
        // TODO: cache ServiceReference? or even DockingAreaContainerProvider?
        // TODO: check if the code is safe, if the services disappear
        BundleContext bundleContext = FrameworkUtil.getBundle(Dockables.class).getBundleContext();

        injectContexts(bundleContext, dockable);
        injectDocking(bundleContext, dockable);
    }

    private static void injectContexts(BundleContext bundleContext, Object target) {
        ServiceReference<ActiveContextProvider> activeContextProviderServiceReference
                = bundleContext.getServiceReference(ActiveContextProvider.class);
        ActiveContextProvider activeContextProvider = bundleContext.getService(activeContextProviderServiceReference);

        ServiceReference<ApplicationContextProvider> applicationContextProviderServiceReference
                = bundleContext.getServiceReference(ApplicationContextProvider.class);
        ApplicationContextProvider applicationContextProvider = bundleContext.getService(
                applicationContextProviderServiceReference);

        ContextInjector contextInjector = new ContextInjector(activeContextProvider, applicationContextProvider);
        contextInjector.inject(target);

        bundleContext.ungetService(applicationContextProviderServiceReference);
        bundleContext.ungetService(activeContextProviderServiceReference);

    }

    private static <D, DATA extends DockableData> void injectDocking(BundleContext bundleContext, D dockable) {
        @SuppressWarnings("rawtypes")
        ServiceReference<DockableDataManagerProvider> dockableDataManagerProviderServiceReference
                = bundleContext.getServiceReference(DockableDataManagerProvider.class);
        @SuppressWarnings("unchecked")
        DockableDataManagerProvider<D, DATA> dockableDataManagerProvider
                = bundleContext.getService(dockableDataManagerProviderServiceReference);

//        @SuppressWarnings("rawtypes")
//        ServiceReference<DockableDataFactory> dockableDataFactoryServiceReference
//                = bundleContext.getServiceReference(DockableDataFactory.class);
//        @SuppressWarnings("unchecked")
//        DockableDataFactory<DATA> dockableDataFactory
//                = bundleContext.getService(dockableDataFactoryServiceReference);
        final DockableDataManager<D, DATA> dockableDataManager = dockableDataManagerProvider.getDockableDataManager();
//        if (dockableDataManager.getDockableData(dockable) == null) {
//            DATA classDockableData = dockableDataManager.getClassDockableData(dockable);
//            if (classDockableData != null) {
//                DATA copyDockableData = dockableDataFactory.copyDockableData(classDockableData);
//                dockableDataManager.registerDockableData(dockable, copyDockableData);
//            }
//        }

        DockingInjector<D, DATA> dockingInjector = new DockingInjector<>(dockableDataManager);
        dockingInjector.inject(dockable);

//        bundleContext.ungetService(dockableDataFactoryServiceReference);
        bundleContext.ungetService(dockableDataManagerProviderServiceReference);
    }
}
