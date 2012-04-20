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
import org.richclientplatform.core.action.jaxb.MenusType;
import org.richclientplatform.core.action.jaxb.ToggleMenuEntryType;
import org.richclientplatform.core.action.spi.ActionRegistry;
import org.richclientplatform.core.action.spi.ToggleMenuEntryDescriptor;
import org.richclientplatform.core.action.spi.ToggleMenuItemFactory;
import org.richclientplatform.core.action.spi.ToggleActionFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "toggleMenuEntryDescriptor", referenceInterface = ToggleMenuEntryDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class ToggleMenuItemHandler<MenuItem, Menu extends MenuItem, ToggleMenuItem extends MenuItem, ToggleAction>
        extends AbstractMenuItemHandler<MenuItem, Menu, ToggleMenuItem, ToggleMenuEntryDescriptor> {

    @Reference
    private ToggleMenuItemFactory<ToggleMenuItem, ToggleAction> menuItemFactory;
    @Reference
    private ToggleActionFactory<ToggleAction> actionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();

    protected void bindToggleMenuEntryDescriptor(ServiceReference<ToggleMenuEntryDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ToggleMenuEntryDescriptor menuEntryDescriptor = context.getService(serviceReference);
        resolveMenuItem(menuEntryDescriptor, context);
    }

    protected void unbindToggleMenuEntryDescriptor(ServiceReference<ToggleMenuEntryDescriptor> serviceReference) {
        // TODO
    }

    protected void bindToggleMenuItemFactory(ToggleMenuItemFactory<ToggleMenuItem, ToggleAction> menuItemFactory) {
        this.menuItemFactory = menuItemFactory;
        resolveUnresolvedItems();
    }

    protected void unbindToggleMenuItemFactory(ToggleMenuItemFactory<ToggleMenuItem, ToggleAction> menuItemFactory) {
        this.menuItemFactory = null;
    }

    protected void bindToggleActionFactory(ToggleActionFactory<ToggleAction> actionFactory) {
        this.actionFactory = actionFactory;
        resolveUnresolvedItems();
    }

    protected void unbindToggleActionFactory(ToggleActionFactory<ToggleAction> actionFactory) {
        this.actionFactory = null;
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && menuItemFactory != null && actionFactory != null;
    }

    @Override
    protected void resolveMenuItem(MenusType menusType, Bundle bundle, BundleContext context) {
        for (ToggleMenuEntryType menuEntry : menusType.getToggleMenuEntry()) {
            ToggleMenuEntryDescriptor menuEntryDescriptor = ToggleMenuEntryDescriptor.createRadioMenuEntryDescriptor(
                    menuEntry);
            resolveMenuItem(menuEntryDescriptor, context);
        }
    }

    @Override
    protected ToggleMenuItem createMenuItem(ToggleMenuEntryDescriptor menuEntryDescriptor, BundleContext context, int iconSize) {
        ToggleAction action = actionRegistry.getAction(menuEntryDescriptor.getActionId(),
                actionFactory.getToggleActionClass(), context);
        ToggleMenuItem menuItem = menuItemFactory.createToggleMenuItem(menuEntryDescriptor, action, iconSize);
        return menuItem;
    }
}
