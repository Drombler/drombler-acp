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

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.acp.core.docking.spi.DockableDataFactory;
import org.drombler.acp.core.docking.spi.DockableDataManagerProvider;
import org.drombler.acp.core.docking.spi.DockablePreferencesManagerProvider;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableDataManager;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockablePreferences;
import org.drombler.commons.docking.DockablePreferencesManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 *
 * @author puce
 */
@References({
    @Reference(name = "dockingsType", referenceInterface = DockingsType.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
})
public abstract class AbstractDockableDockingHandler<D, DATA extends DockableData, E extends DockableEntry<D>> extends AbstractDockingHandler<D, E> {

    @Reference
    private DockablePreferencesManagerProvider<D> dockablePreferencesManagerProvider;

    @Reference
    private DockableDataManagerProvider<D, DATA> dockableDataManagerProvider;

    @Reference
    private DockableDataFactory<DATA> dockableDataFactory;

    protected void bindDockablePreferencesManagerProvider(
            DockablePreferencesManagerProvider<D> dockablePreferencesManagerProvider) {
        this.dockablePreferencesManagerProvider = dockablePreferencesManagerProvider;
    }

    protected void unbindDockablePreferencesManagerProvider(
            DockablePreferencesManagerProvider<D> dockablePreferencesManagerProvider) {
        this.dockablePreferencesManagerProvider = null;
    }

    protected void bindDockableDataManagerProvider(
            DockableDataManagerProvider<D, DATA> dockableDataManagerProvider) {
        this.dockableDataManagerProvider = dockableDataManagerProvider;
    }

    protected void unbindDockableDataManagerProvider(
            DockableDataManagerProvider<D, DATA> dockableDataManagerProvider) {
        this.dockableDataManagerProvider = null;
    }

    protected void bindDockableDataFactory(DockableDataFactory<DATA> dockableDataFactory) {
        this.dockableDataFactory = dockableDataFactory;
    }

    protected void unbindDockableDataFactory(DockableDataFactory<D> dockableDataFactory) {
        this.dockableDataFactory = null;
    }

    protected void bindDockingsType(ServiceReference<DockingsType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        DockingsType dockingsType = context.getService(serviceReference);
        resolveDockingsType(dockingsType, bundle, context);
    }

    protected void unbindDockingsType(DockingsType dockingAreasType) {
        // TODO
    }

    protected abstract void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context);

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && dockablePreferencesManagerProvider != null
                && dockableDataManagerProvider != null && dockableDataFactory != null;
    }

    protected void registerDefaultDockablePreferences(Class<?> dockableClass, DockablePreferences dockablePreferences) {
        getDockablePreferencesManager().registerDefaultDockablePreferences(dockableClass, dockablePreferences);
    }

    protected void registerClassDockableData(Class<?> dockableClass, DATA dockableData) {
        getDockableDataManager().registerClassDockableData(dockableClass, dockableData);
    }

    protected DockablePreferencesManager<D> getDockablePreferencesManager() {
        return dockablePreferencesManagerProvider.getDockablePreferencesManager();
    }

    protected DockableDataManager<D, DATA> getDockableDataManager() {
        return dockableDataManagerProvider.getDockableDataManager();
    }

    protected DockableDataFactory<DATA> getDockableDataFactory() {
        return dockableDataFactory;
    }

    protected DockablePreferences createDockablePreferences(String areaId, int position) {
        DockablePreferences dockablePreferences = new DockablePreferences();
        dockablePreferences.setAreaId(areaId);
        dockablePreferences.setPosition(position);
        return dockablePreferences;
    }
}
