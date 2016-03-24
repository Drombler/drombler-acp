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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.drombler.commons.context.ActiveContextProvider;
import org.drombler.commons.context.ApplicationContextProvider;
import org.drombler.commons.context.ContextInjector;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableDataManager;
import org.drombler.commons.docking.DockableEntry;
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
                dockable,
                dockablePreferencesManagerProvider.getDockablePreferencesManager().getDockablePreferences(dockable)));

        bundleContext.ungetService(dockingAreaContainerProviderServiceReference);
        bundleContext.ungetService(dockablePreferencesManagerProviderServiceReference);
        bundleContext.ungetService(dockableEntryFactoryServiceReference);
    }

    public static <D, E extends DockableEntry<D>> void openEditor(Object content) {
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

        @SuppressWarnings("rawtypes")
        ServiceReference<EditorRegistry> editorRegistryServiceReference
                = bundleContext.getServiceReference(EditorRegistry.class);
        @SuppressWarnings("unchecked")
        EditorRegistry<D> editorRegistry
                = bundleContext.getService(editorRegistryServiceReference);

        Class<? extends D> editorClass = editorRegistry.getEditorClass(content.getClass());
        if (editorClass != null) {
            try {
                D editor = createEditor(editorClass, content);

                dockingAreaContainerProvider.getDockingAreaContainer().addDockable(
                        dockableEntryFactory.createDockableEntry(editor,
                                dockablePreferencesManagerProvider.getDockablePreferencesManager().getDockablePreferences(editor)));
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
        bundleContext.ungetService(editorRegistryServiceReference);
        bundleContext.ungetService(dockingAreaContainerProviderServiceReference);
        bundleContext.ungetService(dockablePreferencesManagerProviderServiceReference);
        bundleContext.ungetService(dockableEntryFactoryServiceReference);
    }

    private static <D> D createEditor(Class<? extends D> editorClass, Object content)
            throws IllegalAccessException, SecurityException, InvocationTargetException, InstantiationException, IllegalArgumentException, NoSuchMethodException {
        Constructor<? extends D> editorConstructor = editorClass.getConstructor(content.getClass());
        D editor = editorConstructor.newInstance(content);
        inject(editor);
        return editor;
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

        @SuppressWarnings("rawtypes")
        ServiceReference<DockableDataFactory> dockableDataFactoryServiceReference
                = bundleContext.getServiceReference(DockableDataFactory.class);
        @SuppressWarnings("unchecked")
        DockableDataFactory<DATA> dockableDataFactory
                = bundleContext.getService(dockableDataFactoryServiceReference);

        final DockableDataManager<D, DATA> dockableDataManager = dockableDataManagerProvider.getDockableDataManager();
        if (dockableDataManager.getDockableData(dockable) == null) {
            DATA classDockableData = dockableDataManager.getClassDockableData(dockable);
            if (classDockableData != null) {
                DATA copyDockableData = dockableDataFactory.copyDockableData(classDockableData);
                dockableDataManager.registerDockableData(dockable, copyDockableData);
            }
        }

        DockingInjector<D, DATA> dockingInjector = new DockingInjector<>(dockableDataManager);
        dockingInjector.inject(dockable);

        bundleContext.ungetService(dockableDataFactoryServiceReference);
        bundleContext.ungetService(dockableDataManagerProviderServiceReference);
    }
}
