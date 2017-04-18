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
package org.drombler.acp.core.action.spi.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.drombler.acp.core.action.jaxb.MenusType;
import org.drombler.acp.core.action.spi.ActionFactory;
import org.drombler.acp.core.action.spi.ActionRegistry;
import org.drombler.acp.core.action.spi.MenuEntryDescriptor;
import org.drombler.acp.core.action.spi.MenuItemFactory;
import org.drombler.acp.core.commons.util.UnresolvedEntry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "menuEntryDescriptor", referenceInterface = MenuEntryDescriptor.class,
        cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class MenuItemHandler<MenuItem, Menu extends MenuItem, Action> extends AbstractMenuItemHandler<MenuItem, Menu, MenuItem, MenuEntryDescriptor<MenuItem, ?>, MenuItemConfig<Action>> {

    @Reference
    private MenuItemFactory<MenuItem, Action> menuItemFactory;
    @Reference
    private ActionFactory<Action> actionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();
    private final ActionResolutionManager<MenuEntryDescriptor> actionResolutionManager = new ActionResolutionManager<>();
    private ServiceTracker<Action, ServiceReference<Action>> tracker;

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
    }

    protected void unbindMenuItemFactory(MenuItemFactory<MenuItem, Action> menuItemFactory) {
        this.menuItemFactory = null;
    }

    protected void bindActionFactory(ActionFactory<Action> actionFactory) {
        this.actionFactory = actionFactory;
    }

    protected void unbindActionFactory(ActionFactory<Action> actionFactory) {
        this.actionFactory = null;
    }

    @Activate
    protected void activate(ComponentContext context) {
        tracker = createActionTracker(context);
        tracker.open();
        resolveUnresolvedItems();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        tracker.close();
    }

    private ServiceTracker<Action, ServiceReference<Action>> createActionTracker(ComponentContext context) {
        return new ServiceTracker<>(context.getBundleContext(), actionFactory.getActionClass(),
                new ServiceTrackerCustomizer<Action, ServiceReference<Action>>() {

                    @Override
                    public ServiceReference<Action> addingService(ServiceReference<Action> reference) {
                        String actionId = actionRegistry.getActionId(reference);
                        if (actionResolutionManager.containsUnresolvedEntries(actionId)) {
                            actionResolutionManager.removeUnresolvedEntries(actionId).forEach(unresolvedEntry
                                    -> resolveMenuItem(unresolvedEntry.getEntry(), unresolvedEntry.getContext()));
                        }
                        return reference;
                    }

                    @Override
                    public void modifiedService(ServiceReference<Action> reference, ServiceReference<Action> service) {
                        // TODO ???
                    }

            @Override
            public void removedService(ServiceReference<Action> reference, ServiceReference<Action> service) {
                // TODO ???
            }
        });
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && menuItemFactory != null && actionFactory != null;
    }

    @Override
    protected void resolveMenuItem(MenusType menusType, Bundle bundle, BundleContext context) {
        menusType.getMenuEntry().stream()
                .map(MenuEntryDescriptor::createMenuEntryDescriptor)
                .forEach(menuEntryDescriptor -> resolveMenuItem((MenuEntryDescriptor<MenuItem, ?>) menuEntryDescriptor, context)); // TODO: possible to avoid cast?
    }

    @Override
    protected MenuItemConfig<Action> createConfig(MenuEntryDescriptor<MenuItem, ?> menuEntryDescriptor, BundleContext context) {
        Action action = actionRegistry.getAction(menuEntryDescriptor.getActionId(), actionFactory.getActionClass(), context);
        if (action != null) {
            return new MenuItemConfig<>(action);
        } else {
            return null;
        }
    }

    @Override
    protected MenuItem createMenuItem(MenuEntryDescriptor<MenuItem, ?> menuEntryDescriptor, MenuItemConfig<Action> config) {
//        System.out.println(actionFactory.getActionClass().getName() + ": " + menuEntryDescriptor.getActionId());
        return menuItemFactory.createMenuItem(config.getAction(), config.getIconSize());

    }

    @Override
    protected void registerUnresolvedMenuItem(MenuEntryDescriptor<MenuItem, ?> menuEntryDescriptor, BundleContext context) {
        actionResolutionManager.addUnresolvedEntry(menuEntryDescriptor.getActionId(),
                new UnresolvedEntry<>(menuEntryDescriptor, context));
    }
}
