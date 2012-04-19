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
import org.richclientplatform.core.action.jaxb.CheckMenuEntryType;
import org.richclientplatform.core.action.jaxb.MenusType;
import org.richclientplatform.core.action.spi.ActionRegistry;
import org.richclientplatform.core.action.spi.CheckActionFactory;
import org.richclientplatform.core.action.spi.CheckMenuEntryDescriptor;
import org.richclientplatform.core.action.spi.CheckMenuItemFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "checkMenuEntryDescriptor", referenceInterface = CheckMenuEntryDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class CheckMenuItemHandler<MenuItem, Menu extends MenuItem, CheckMenuItem extends MenuItem, CheckAction>
        extends AbstractMenuItemHandler<MenuItem, Menu, CheckMenuItem, CheckMenuEntryDescriptor> {

    @Reference
    private CheckMenuItemFactory<CheckMenuItem, CheckAction> menuItemFactory;
    @Reference
    private CheckActionFactory<CheckAction> actionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();

    protected void bindCheckMenuEntryDescriptor(ServiceReference<CheckMenuEntryDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        CheckMenuEntryDescriptor menuEntryDescriptor = context.getService(serviceReference);
        resolveMenuItem(menuEntryDescriptor, context);
    }

    protected void unbindCheckMenuEntryDescriptor(ServiceReference<CheckMenuEntryDescriptor> serviceReference) {
        // TODO
    }

    protected void bindCheckMenuItemFactory(CheckMenuItemFactory<CheckMenuItem, CheckAction> menuItemFactory) {
        this.menuItemFactory = menuItemFactory;
        resolveUnresolvedItems();
    }

    protected void unbindCheckMenuItemFactory(CheckMenuItemFactory<CheckMenuItem, CheckAction> menuItemFactory) {
        this.menuItemFactory = null;
    }

    protected void bindCheckActionFactory(CheckActionFactory<CheckAction> actionFactory) {
        this.actionFactory = actionFactory;
        resolveUnresolvedItems();
    }

    protected void unbindCheckActionFactory(CheckActionFactory<CheckAction> actionFactory) {
        this.actionFactory = null;
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && menuItemFactory != null && actionFactory != null;
    }

    @Override
    protected void resolveMenuItem(MenusType menusType, Bundle bundle, BundleContext context) {
        for (CheckMenuEntryType menuEntry : menusType.getCheckMenuEntry()) {
            CheckMenuEntryDescriptor menuEntryDescriptor = CheckMenuEntryDescriptor.createCheckMenuEntryDescriptor(
                    menuEntry);
            resolveMenuItem(menuEntryDescriptor, context);
        }
    }

    @Override
    protected CheckMenuItem createMenuItem(CheckMenuEntryDescriptor menuEntryDescriptor, BundleContext context, int iconSize) {
        CheckAction action = actionRegistry.getAction(menuEntryDescriptor.getActionId(),
                actionFactory.getCheckActionClass(), context);
        return menuItemFactory.createCheckMenuItem(menuEntryDescriptor, action, iconSize);
    }

}
