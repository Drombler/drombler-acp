/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking.spi.impl;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.acp.core.docking.spi.DockablePreferences;
import org.drombler.acp.core.docking.spi.DockablePreferencesManager;

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

    protected void unbindDockablePreferencesManager(DockablePreferencesManager dockablePreferencesManager) {
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
