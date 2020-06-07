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

import org.drombler.acp.core.action.jaxb.MenusType;
import org.drombler.acp.core.action.spi.*;
import org.drombler.acp.core.commons.util.UnresolvedEntry;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.*;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 *
 * @author puce
 */
@Component(immediate = true)
public class ToggleMenuItemHandler<MenuItem, Menu extends MenuItem, ToggleMenuItem extends MenuItem, ToggleAction>
        extends AbstractMenuItemHandler<MenuItem, Menu, ToggleMenuItem, ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem, ?>, MenuItemConfig<ToggleAction>> {

    @Reference
    private ToggleMenuItemFactory<MenuItem, ToggleMenuItem, ToggleAction> toggleMenuItemFactory;
    @Reference
    private ToggleActionFactory<ToggleAction> toggleActionFactory;
    private final ActionRegistry<?> actionRegistry = new ActionRegistry<>(ActionDescriptor.class);
    private final ActionResolutionManager<ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem, ?>> actionResolutionManager = new ActionResolutionManager<>();
    private ServiceTracker<ToggleAction, ServiceReference<ToggleAction>> tracker;

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindToggleMenuEntryDescriptor(ServiceReference<ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem, ?>> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem, ?> menuEntryDescriptor = context.getService(serviceReference);
        resolveMenuItem(menuEntryDescriptor, context);
    }

    protected void unbindToggleMenuEntryDescriptor(ServiceReference<ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem, ?>> serviceReference) {
        // TODO
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

    private ServiceTracker<ToggleAction, ServiceReference<ToggleAction>> createActionTracker(ComponentContext context) {
        return new ServiceTracker<>(context.getBundleContext(), toggleActionFactory.getToggleActionClass(),
                new ServiceTrackerCustomizer<>() {
                    @Override
                    public ServiceReference<ToggleAction> addingService(ServiceReference<ToggleAction> reference) {
                        String actionId = actionRegistry.getActionId(reference);
                        if (actionResolutionManager.containsUnresolvedEntries(actionId)) {
                            actionResolutionManager.removeUnresolvedEntries(actionId).forEach(unresolvedEntry
                                    -> resolveMenuItem(unresolvedEntry.getEntry(), unresolvedEntry.getContext()));
                        }
                        return reference;
                    }

                    @Override
                    public void modifiedService(ServiceReference<ToggleAction> reference,
                            ServiceReference<ToggleAction> service) {
                        // TODO ???
                    }

                    @Override
                    public void removedService(ServiceReference<ToggleAction> reference,
                            ServiceReference<ToggleAction> service) {
                        // TODO ???
                    }
                });
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && toggleMenuItemFactory != null && toggleActionFactory != null;
    }

    @Override
    protected void resolveMenuItem(MenusType menusType, Bundle bundle, BundleContext context) {
        menusType.getToggleMenuEntry().stream()
                .map(ToggleMenuEntryDescriptor::createToggleMenuEntryDescriptor)
                .forEach(menuEntryDescriptor -> resolveMenuItem((ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem, ?>) menuEntryDescriptor, context)); // TODO: possible to avoid cast?
    }

    @Override
    protected MenuItemConfig<ToggleAction> createConfig(ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem, ?> menuEntryDescriptor,
            BundleContext context) {
        ToggleAction action = actionRegistry.getAction(menuEntryDescriptor.getActionId(),
                toggleActionFactory.getToggleActionClass(), context);
        if (action != null) {
            return new MenuItemConfig<>(action);
        } else {
            return null;
        }
    }

    @Override
    protected ToggleMenuItem createMenuItem(ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem, ?> menuEntryDescriptor,
            MenuItemConfig<ToggleAction> config) {
//        System.out.println(actionFactory.getToggleActionClass().getName() + ": " + menuEntryDescriptor.getActionId());
        return toggleMenuItemFactory.createToggleMenuItem(menuEntryDescriptor, config.getAction(), config.getIconSize());
    }

    @Override
    protected void registerUnresolvedMenuItem(ToggleMenuEntryDescriptor<MenuItem, ToggleMenuItem, ?> menuEntryDescriptor, BundleContext context) {
        actionResolutionManager.addUnresolvedEntry(menuEntryDescriptor.getActionId(),
                new UnresolvedEntry<>(menuEntryDescriptor, context));
    }
}
