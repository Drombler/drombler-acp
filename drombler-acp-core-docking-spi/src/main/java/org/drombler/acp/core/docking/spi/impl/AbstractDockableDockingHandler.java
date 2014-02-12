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
import org.drombler.acp.core.docking.spi.DockablePreferencesManagerProvider;
import org.drombler.commons.client.docking.DockablePreferences;
import org.drombler.commons.client.docking.DockablePreferencesManager;
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
public abstract class AbstractDockableDockingHandler<D> extends AbstractDockingHandler<D> {

    @Reference
    private DockablePreferencesManagerProvider<D> dockablePreferencesManagerProvider;

    protected void bindDockablePreferencesManagerProvider(
            DockablePreferencesManagerProvider<D> dockablePreferencesManagerProvider) {
        this.dockablePreferencesManagerProvider = dockablePreferencesManagerProvider;
    }

    protected void bindDockingsType(ServiceReference<DockingsType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        DockingsType dockingsType = context.getService(serviceReference);
        resolveDockingsType(dockingsType, bundle, context);
    }

    protected abstract void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context);

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && dockablePreferencesManagerProvider != null;
    }

    protected void unbindDockablePreferencesManagerProvider(
            DockablePreferencesManagerProvider<D> dockablePreferencesManagerProvider) {
        this.dockablePreferencesManagerProvider = null;
    }

    protected void unbindDockingsType(DockingsType dockingAreasType) {
        // TODO
    }

    protected void registerDefaultDockablePreferences(Class<?> dockableClass, DockablePreferences dockablePreferences) {
        getDockablePreferencesManager().registerDefaultDockablePreferences(dockableClass, dockablePreferences);
    }

    protected DockablePreferencesManager<D> getDockablePreferencesManager() {
        return dockablePreferencesManagerProvider.getDockablePreferencesManager();
    }

    protected DockablePreferences createDockablePreferences(String areaId, int position) {
        DockablePreferences dockablePreferences = new DockablePreferences();
        dockablePreferences.setAreaId(areaId);
        dockablePreferences.setPosition(position);
        return dockablePreferences;
    }
}
