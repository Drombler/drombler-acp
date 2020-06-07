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

import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 *
 * @author puce
 */
public abstract class AbstractDockableDockingHandler<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> extends AbstractDockingHandler<D, DATA, E> {

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindDockingsType(ServiceReference<DockingsType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        DockingsType dockingsType = context.getService(serviceReference);
        resolveDockingsType(dockingsType, bundle, context);
    }

    protected void unbindDockingsType(ServiceReference<DockingsType> serviceReference) throws Exception {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        DockingsType dockingsType = context.getService(serviceReference);
        unregisterDockingsType(dockingsType, bundle);
    }

    protected abstract void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context);

    protected abstract void unregisterDockingsType(DockingsType dockingsType, Bundle bundle) throws Exception;

}
