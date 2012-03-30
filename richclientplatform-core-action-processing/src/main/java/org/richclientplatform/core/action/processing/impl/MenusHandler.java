/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.processing.impl;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.action.processing.ActionDescriptor;
import org.richclientplatform.core.action.jaxb.MenusType;
import org.richclientplatform.core.action.jaxb.MenuEntryType;
import org.richclientplatform.core.action.jaxb.MenuType;
import org.richclientplatform.core.action.processing.MenuDescriptor;
import org.richclientplatform.core.action.processing.MenuEntryDescriptor;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "menusType", referenceInterface = MenusType.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class MenusHandler {

    protected void bindMenusType(ServiceReference<MenusType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        MenusType menusType = context.getService(serviceReference);

        for (MenuType menu : menusType.getMenu()) {
            MenuDescriptor menuDescriptor = MenuDescriptor.createMenuDescriptor(menu, bundle);
            context.registerService(MenuDescriptor.class, menuDescriptor, null);
        }

        for (MenuEntryType menuEntry : menusType.getMenuEntry()) {
            MenuEntryDescriptor menuEntryDescriptor = MenuEntryDescriptor.createMenuEntryDescriptor(menuEntry);
            context.registerService(MenuEntryDescriptor.class, menuEntryDescriptor, null);
        }

    }

    protected void unbindMenusType(MenusType menusType) {
        // TODO
    }
}
