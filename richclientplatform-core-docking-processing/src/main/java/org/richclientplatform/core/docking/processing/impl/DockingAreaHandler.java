/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.docking.jaxb.DockingAreaType;
import org.richclientplatform.core.docking.jaxb.DockingAreasType;
import org.richclientplatform.core.docking.processing.DockingAreaDescriptor;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "dockingAreasType", referenceInterface = DockingAreasType.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class DockingAreaHandler {

    protected void bindDockingAreasType(ServiceReference<DockingAreasType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        DockingAreasType dockingAreasType = context.getService(serviceReference);
        for (DockingAreaType dockingArea : dockingAreasType.getDockingArea()) {
            DockingAreaDescriptor dockingAreaDescriptor = DockingAreaDescriptor.createDockingAreaDescriptor(dockingArea);
            context.registerService(DockingAreaDescriptor.class, dockingAreaDescriptor, null);
        }
    }

    protected void unbindDockingAreasType(DockingAreasType dockingAreasType) {
        // TODO
    }
}
