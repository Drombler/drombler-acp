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
import org.drombler.acp.core.docking.spi.DockablePreferences;
import org.drombler.acp.core.docking.spi.DockablePreferencesManager;
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
public abstract class AbstractDockableDockingHandler<A, D> extends AbstractDockingHandler<A, D> {

    @Reference
    private DockablePreferencesManager<D> dockablePreferencesManager;

    protected void bindDockablePreferencesManager(DockablePreferencesManager<D> dockablePreferencesManager) {
        this.dockablePreferencesManager = dockablePreferencesManager;
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
        return super.isInitialized() && dockablePreferencesManager != null;
    }

    protected void unbindDockablePreferencesManager(DockablePreferencesManager<D> dockablePreferencesManager) {
        this.dockablePreferencesManager = null;
    }

    protected void unbindDockingsType(DockingsType dockingAreasType) {
        // TODO
    }

    protected void registerDockablePreferences(Class<?> dockableClass, String areaId, int position) {
        DockablePreferences dockablePreferences = new DockablePreferences();
        dockablePreferences.setAreaId(areaId);
        dockablePreferences.setPosition(position);
        dockablePreferencesManager.registerDefaultDockablePreferences(dockableClass, dockablePreferences);
    }
}
