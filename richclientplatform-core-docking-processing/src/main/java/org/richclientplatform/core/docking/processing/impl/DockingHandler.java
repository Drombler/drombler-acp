/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.action.jaxb.ActionType;
import org.richclientplatform.core.action.jaxb.MenuEntryType;
import org.richclientplatform.core.docking.jaxb.DockingType;
import org.richclientplatform.core.docking.jaxb.DockingsType;
import org.richclientplatform.core.docking.processing.DockingDescriptor;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "dockingsType", referenceInterface = DockingsType.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class DockingHandler {

    public static final String WINDOW_MENU_ID = "Window";

    protected void bindDockingsType(ServiceReference<DockingsType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        DockingsType dockingsType = context.getService(serviceReference);
        for (DockingType dockingType : dockingsType.getDocking()) {
            try {
                DockingDescriptor dockingDescriptor = DockingDescriptor.createDockingDescriptor(dockingType, bundle);
                context.registerService(DockingDescriptor.class, dockingDescriptor, null);

//            ActionType action = createAction(dockingType);
//            context.registerService(ActionType.class, action, null);

//            MenuEntryType menuEntry = createMenuEntry(dockingType, action.getId());
//            context.registerService(MenuEntryType.class, menuEntry, null);
            } catch (Exception ex) {
                Logger.getLogger(DockingHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void unbindDockingsType(DockingsType dockingAreasType) {
        // TODO
    }
//    private ActionType createAction(DockingsType dockingType) {
//        ActionType action = new ActionType();
//        action.setId(dockingType.getId());
//        action.setCategory(dockingType);
//        return action;
//    }
}
