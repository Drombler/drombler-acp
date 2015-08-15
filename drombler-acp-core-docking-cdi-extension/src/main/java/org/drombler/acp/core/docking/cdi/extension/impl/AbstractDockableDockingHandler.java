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


import org.drombler.commons.client.docking.DockableData;
import org.drombler.commons.client.docking.DockableEntry;
import org.drombler.commons.client.docking.DockablePreferences;


/**
 *
 * @author puce
 */
public abstract class AbstractDockableDockingHandler<D, DATA extends DockableData, E extends DockableEntry<D>> extends AbstractDockingHandler<D, E> {

//    @Inject
//    @OsgiService
//    private DockablePreferencesManagerProvider<D> dockablePreferencesManagerProvider;
//
//    @Inject
//    @OsgiService
//    private DockableDataManagerProvider<D, DATA> dockableDataManagerProvider;
//
//    @Inject
//    @OsgiService
//    private DockableDataFactory<DATA> dockableDataFactory;

    
//
//    protected void bindDockingsType(ServiceReference<DockingsType> serviceReference) {
//        Bundle bundle = serviceReference.getBundle();
//        BundleContext context = bundle.getBundleContext();
//        DockingsType dockingsType = context.getService(serviceReference);
//        resolveDockingsType(dockingsType, bundle, context);
//    }

//    protected abstract void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context);

    // TODO: needed?
//    @Override
//    protected boolean isInitialized() {
//        return super.isInitialized() && dockablePreferencesManagerProvider != null
//                && dockableDataManagerProvider != null && dockableDataFactory != null;
//    }
//
//    protected void registerDefaultDockablePreferences(Class<?> dockableClass, DockablePreferences dockablePreferences) {
//        getDockablePreferencesManager().registerDefaultDockablePreferences(dockableClass, dockablePreferences);
//    }
//
//    protected void registerClassDockableData(Class<?> dockableClass, DATA dockableData) {
//        getDockableDataManager().registerClassDockableData(dockableClass, dockableData);
//    }
//
//    protected DockablePreferencesManager<D> getDockablePreferencesManager() {
//        return dockablePreferencesManagerProvider.getDockablePreferencesManager();
//    }
//
//    protected DockableDataManager<D, DATA> getDockableDataManager() {
//        return dockableDataManagerProvider.getDockableDataManager();
//    }
//
//    protected DockableDataFactory<DATA> getDockableDataFactory() {
//        return dockableDataFactory;
//    }

    protected DockablePreferences createDockablePreferences(String areaId, int position) {
        DockablePreferences dockablePreferences = new DockablePreferences();
        dockablePreferences.setAreaId(areaId);
        dockablePreferences.setPosition(position);
        return dockablePreferences;
    }
}
