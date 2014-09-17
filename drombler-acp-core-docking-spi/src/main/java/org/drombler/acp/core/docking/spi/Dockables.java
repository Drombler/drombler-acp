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

import org.drombler.commons.client.docking.DockableData;
import org.drombler.commons.client.docking.DockableDataManager;
import org.drombler.commons.client.docking.DockableEntry;
import org.drombler.commons.client.docking.DockingInjector;
import org.drombler.commons.context.ActiveContextProvider;
import org.drombler.commons.context.ApplicationContextProvider;
import org.drombler.commons.context.ContextInjector;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * TODO: Move to API? But DockingAreaContainerProvider is a SPI interface...
 *
 * @author puce
 */
public final class Dockables {

    private Dockables() {
    }

    public static <D, DATA extends DockableData, E extends DockableEntry<D, DATA>> void open(D dockable) {
        // TODO: cache ServiceReference? or even DockingAreaContainerProvider?
        // TODO: check if the code is safe, if the services disappear
        BundleContext bundleContext = FrameworkUtil.getBundle(Dockables.class).getBundleContext();

        ServiceReference<DockablePreferencesManagerProvider> dockablePreferencesManagerProviderServiceReference
                = bundleContext.getServiceReference(DockablePreferencesManagerProvider.class);
        @SuppressWarnings("unchecked")
        DockablePreferencesManagerProvider<D> dockablePreferencesManagerProvider
                = bundleContext.getService(dockablePreferencesManagerProviderServiceReference);

        ServiceReference<DockableDataManagerProvider> dockableDataManagerProviderServiceReference
                = bundleContext.getServiceReference(DockableDataManagerProvider.class);
        @SuppressWarnings("unchecked")
        DockableDataManagerProvider<D, DATA> dockableDataManagerProvider
                = bundleContext.getService(dockableDataManagerProviderServiceReference);

        ServiceReference<DockingAreaContainerProvider> dockingAreaContainerProviderServiceReference
                = bundleContext.getServiceReference(DockingAreaContainerProvider.class);
        @SuppressWarnings("unchecked")
        DockingAreaContainerProvider<D, DATA, E> dockingPaneProvider
                = bundleContext.getService(dockingAreaContainerProviderServiceReference);

        ServiceReference<DockableEntryFactory> dockableEntryFactoryServiceReference
                = bundleContext.getServiceReference(DockableEntryFactory.class);
        @SuppressWarnings("unchecked")
        DockableEntryFactory<D, DATA, E> dockableEntryFactory
                = bundleContext.getService(dockableEntryFactoryServiceReference);
        DATA dockableData = dockableDataManagerProvider.getDockableDataManager().getDockableData(dockable);

        dockingPaneProvider.getDockingAreaContainer().addDockable(dockableEntryFactory.createDockableEntry(dockable,
                dockableData,
                dockablePreferencesManagerProvider.getDockablePreferencesManager().getDockablePreferences(dockable)));

        bundleContext.ungetService(dockingAreaContainerProviderServiceReference);
        bundleContext.ungetService(dockableDataManagerProviderServiceReference);
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
        ServiceReference<DockableDataManagerProvider> dockableDataManagerProviderServiceReference
                = bundleContext.getServiceReference(DockableDataManagerProvider.class);
        @SuppressWarnings("unchecked")
        DockableDataManagerProvider<D, DATA> dockableDataManagerProvider
                = bundleContext.getService(dockableDataManagerProviderServiceReference);

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
