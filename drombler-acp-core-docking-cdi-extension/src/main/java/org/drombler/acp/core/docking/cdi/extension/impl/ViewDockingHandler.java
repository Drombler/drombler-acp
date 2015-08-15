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
package org.drombler.acp.core.docking.cdi.extension.impl;

import java.util.concurrent.Executor;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.drombler.acp.core.application.ApplicationExecutorProvider;
import org.drombler.commons.client.docking.DockableData;
import org.drombler.commons.client.docking.DockableEntry;
import org.ops4j.pax.cdi.api.OsgiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: thread-safe???
 *
 * @author puce
 */
//@ApplicationScoped
@Dependent
//@OsgiServiceProvider
public class ViewDockingHandler<D, DATA extends DockableData, E extends DockableEntry<D>> extends AbstractDockableDockingHandler<D, DATA, E> {

    private static final Logger LOG = LoggerFactory.getLogger(ViewDockingHandler.class);

    @Inject
    @OsgiService(dynamic = true)
    private ApplicationExecutorProvider applicationExecutorProvider;

//    @Inject
//    @OsgiService
//    private ActiveContextProvider activeContextProvider;
//    @Inject
//    @OsgiService
//    private ApplicationContextProvider applicationContextProvider;
//    @Inject
//    @OsgiService
//    private DockableFactory<D> dockableFactory;
//    @Inject
//    @OsgiService
//    private DockableEntryFactory<D, E> dockableEntryFactory;
    private Executor applicationExecutor;
    private ViewDockingManager<D, DATA, E> viewDockingManager;
//    private final List<UnresolvedEntry<ViewDockingDescriptor>> unresolvedDockingDescriptors = new ArrayList<>();



//    protected void bindViewDockingDescriptor(ServiceReference<ViewDockingDescriptor> serviceReference) {
//        BundleContext context = serviceReference.getBundle().getBundleContext();
//        ViewDockingDescriptor dockingDescriptor = context.getService(serviceReference);
//        resolveDockable(dockingDescriptor, context);
//    }



    // TODO: public?
    @PostConstruct
    public void init() {
        System.out.println("Test");
//        viewDockingManager = new ViewDockingManager<>(dockableFactory, getDockableDataFactory(), dockableEntryFactory,
//                new ContextInjector(activeContextProvider, applicationContextProvider),
//                getDockingAreaContainerProvider().getDockingAreaContainer(), getDockableDataManager(),
//                getDockablePreferencesManager());
//        resolveUnresolvedDockables();
    }

    @PreDestroy
    protected void close() {
        viewDockingManager.close();
        viewDockingManager = null;
    }

//    @Override
//    protected boolean isInitialized() {
//        return super.isInitialized() && dockableFactory != null && dockableEntryFactory != null
//                && applicationExecutor != null && activeContextProvider != null && applicationContextProvider != null;
//    }

//    @Override
//    protected void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context) {
//        dockingsType.getViewDocking().forEach(dockingType -> {
//            try {
//                ViewDockingDescriptor dockingDescriptor = ViewDockingDescriptor.createViewDockingDescriptor(dockingType,
//                        bundle);
//                // TODO: register ViewDockingDescriptor as service? Omit resolveDockable?
//                resolveDockable(dockingDescriptor, context);
//            } catch (Exception ex) {
//                LOG.error(ex.getMessage(), ex);
//            }
//        });
//    }
//
//    private void resolveDockable(final ViewDockingDescriptor dockingDescriptor, final BundleContext context) {
//        if (isInitialized()) {
//            resolveDockableBasic(dockingDescriptor);
//            addDockable(dockingDescriptor, context);
//        } else {
//            unresolvedDockingDescriptors.add(new UnresolvedEntry<>(dockingDescriptor, context));
//        }
//    }

//    private void resolveDockableBasic(final ViewDockingDescriptor dockingDescriptor) {
//        registerDefaultDockablePreferences(dockingDescriptor);
//    }

//    private void addDockable(final ViewDockingDescriptor dockingDescriptor, final BundleContext context) {
//        applicationExecutor.execute(() -> viewDockingManager.addDockable(dockingDescriptor, context));
//    }

//    private void addDockables(final List<UnresolvedEntry<ViewDockingDescriptor>> unresolvedDockingDescriptors) {
//        applicationExecutor.execute(()
//                -> unresolvedDockingDescriptors.forEach(unresolvedEntry
//                        -> viewDockingManager.addDockable(unresolvedEntry.getEntry(), unresolvedEntry.getContext()))
//        );
//    }

//    private void registerDefaultDockablePreferences(ViewDockingDescriptor dockingDescriptor) {
//        DockablePreferences dockablePreferences = createDockablePreferences(dockingDescriptor.
//                getAreaId(), dockingDescriptor.getPosition());
//        registerDefaultDockablePreferences(dockingDescriptor.getDockableClass(), dockablePreferences);
//    }

//    private void registerDefaultDockablePreferences(
//            List<UnresolvedEntry<ViewDockingDescriptor>> unresolvedDockingDescriptors) {
//        unresolvedDockingDescriptors.forEach(unresolvedEntry -> resolveDockableBasic(unresolvedEntry.getEntry()));
//    }
//
//    private void resolveUnresolvedDockables() {
//        if (isInitialized()) {
//            List<UnresolvedEntry<ViewDockingDescriptor>> unresolvedDockingDescriptorsCopy
//                    = new ArrayList<>(unresolvedDockingDescriptors);
//            unresolvedDockingDescriptors.clear();
//            registerDefaultDockablePreferences(unresolvedDockingDescriptorsCopy);
//            addDockables(unresolvedDockingDescriptorsCopy);
//        }
//    }

}
