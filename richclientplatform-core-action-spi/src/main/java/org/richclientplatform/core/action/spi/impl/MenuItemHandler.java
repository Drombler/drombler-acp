/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.action.jaxb.MenuEntryType;
import org.richclientplatform.core.action.jaxb.MenusType;
import org.richclientplatform.core.action.spi.ActionFactory;
import org.richclientplatform.core.action.spi.ActionRegistry;
import org.richclientplatform.core.action.spi.MenuEntryDescriptor;
import org.richclientplatform.core.action.spi.MenuItemFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "menuEntryDescriptor", referenceInterface = MenuEntryDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class MenuItemHandler<MenuItem, Menu extends MenuItem, Action> extends AbstractMenuItemHandler<MenuItem, Menu, MenuItem, MenuEntryDescriptor> {

    @Reference
    private MenuItemFactory<MenuItem, Action> menuItemFactory;
    @Reference
    private ActionFactory<Action> actionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();

    protected void bindMenuEntryDescriptor(ServiceReference<MenuEntryDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        MenuEntryDescriptor menuEntryDescriptor = context.getService(serviceReference);
        resolveMenuItem(menuEntryDescriptor, context);
    }

    protected void unbindMenuEntryDescriptor(ServiceReference<MenuEntryDescriptor> serviceReference) {
        // TODO
    }

    protected void bindMenuItemFactory(MenuItemFactory<MenuItem, Action> menuItemFactory) {
        this.menuItemFactory = menuItemFactory;
        resolveUnresolvedItems();
    }

    protected void unbindMenuItemFactory(MenuItemFactory<MenuItem, Action> menuItemFactory) {
        this.menuItemFactory = null;
    }

    protected void bindActionFactory(ActionFactory<Action> actionFactory) {
        this.actionFactory = actionFactory;
        resolveUnresolvedItems();
    }

    protected void unbindActionFactory(ActionFactory<Action> actionFactory) {
        this.actionFactory = null;
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && menuItemFactory != null && actionFactory != null;
    }

    @Override
    protected void resolveMenuItem(MenusType menusType, Bundle bundle, BundleContext context) {
        for (MenuEntryType menuEntry : menusType.getMenuEntry()) {
            MenuEntryDescriptor menuEntryDescriptor = MenuEntryDescriptor.createMenuEntryDescriptor(menuEntry);
            resolveMenuItem(menuEntryDescriptor, context);
        }
    }

    @Override
    protected MenuItem createMenuItem(MenuEntryDescriptor menuEntryDescriptor, BundleContext context, int iconSize) {
        Action action = actionRegistry.getAction(menuEntryDescriptor.getActionId(), actionFactory.getActionClass(),
                context);
        return menuItemFactory.createMenuItem(menuEntryDescriptor, action, iconSize);
    }

}
