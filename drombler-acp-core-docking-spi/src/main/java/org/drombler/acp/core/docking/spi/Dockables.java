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

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * TODO: Move to API? But DockingAreaContainerProvider is a SPI interface...
 *
 * @author puce
 */
public class Dockables {

    private Dockables() {
    }

    public static <D> void open(D dockable) {
        // TODO: cache ServiceReference? or even DockingAreaContainerProvider?
        // TODO: check if the code is safe, if the services disappear
        BundleContext bundleContext = FrameworkUtil.getBundle(Dockables.class).getBundleContext();

        ServiceReference<DockablePreferencesManagerProvider> dockablePreferencesManagerProviderServiceReference
                = bundleContext.getServiceReference(DockablePreferencesManagerProvider.class);
        @SuppressWarnings("unchecked")
        DockablePreferencesManagerProvider<D> dockablePreferencesManagerProvider
                = bundleContext.getService(dockablePreferencesManagerProviderServiceReference);

        ServiceReference<DockingAreaContainerProvider> dockingAreaContainerProviderServiceReference
                = bundleContext.getServiceReference(DockingAreaContainerProvider.class);
        @SuppressWarnings("unchecked")
        DockingAreaContainerProvider<D> dockingPaneProvider
                = bundleContext.getService(dockingAreaContainerProviderServiceReference);

        dockingPaneProvider.getDockingAreaContainer().addDockable(dockable, dockablePreferencesManagerProvider.
                getDockablePreferencesManager().getDockablePreferences(dockable));

        bundleContext.ungetService(dockingAreaContainerProviderServiceReference);
        bundleContext.ungetService(dockablePreferencesManagerProviderServiceReference);
    }
}
