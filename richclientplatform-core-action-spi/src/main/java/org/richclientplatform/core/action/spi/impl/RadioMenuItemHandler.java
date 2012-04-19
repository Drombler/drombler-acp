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
import org.richclientplatform.core.action.jaxb.RadioMenuEntryType;
import org.richclientplatform.core.action.spi.ActionRegistry;
import org.richclientplatform.core.action.spi.RadioMenuEntryDescriptor;
import org.richclientplatform.core.action.spi.RadioMenuItemFactory;
import org.richclientplatform.core.action.spi.ToggleActionFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "radioMenuEntryDescriptor", referenceInterface = RadioMenuEntryDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class RadioMenuItemHandler<MenuItem, Menu extends MenuItem, RadioMenuItem extends MenuItem, ToggleAction>
        extends AbstractMenuItemHandler<MenuItem, Menu, RadioMenuItem, RadioMenuEntryDescriptor> {

    @Reference
    private RadioMenuItemFactory<RadioMenuItem, ToggleAction> menuItemFactory;
    @Reference
    private ToggleActionFactory<ToggleAction> actionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();

    protected void bindRadioMenuEntryDescriptor(ServiceReference<RadioMenuEntryDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        RadioMenuEntryDescriptor menuEntryDescriptor = context.getService(serviceReference);
        resolveMenuItem(menuEntryDescriptor, context);
    }

    protected void unbindRadioMenuEntryDescriptor(ServiceReference<RadioMenuEntryDescriptor> serviceReference) {
        // TODO
    }

    protected void bindRadioMenuItemFactory(RadioMenuItemFactory<RadioMenuItem, ToggleAction> menuItemFactory) {
        this.menuItemFactory = menuItemFactory;
        resolveUnresolvedItems();
    }

    protected void unbindRadioMenuItemFactory(RadioMenuItemFactory<RadioMenuItem, ToggleAction> menuItemFactory) {
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
        for (RadioMenuEntryType menuEntry : menusType.getRadioMenuEntry()) {
            RadioMenuEntryDescriptor menuEntryDescriptor = RadioMenuEntryDescriptor.createRadioMenuEntryDescriptor(
                    menuEntry);
            resolveMenuItem(menuEntryDescriptor, context);
        }
    }

    @Override
    protected RadioMenuItem createMenuItem(RadioMenuEntryDescriptor menuEntryDescriptor, BundleContext context, int iconSize) {
        ToggleAction action = actionRegistry.getAction(menuEntryDescriptor.getActionId(),
                actionFactory.getToggleActionClass(), context);
        RadioMenuItem menuItem = menuItemFactory.createRadioMenuItem(menuEntryDescriptor, action, iconSize);
        return menuItem;
    }
}
