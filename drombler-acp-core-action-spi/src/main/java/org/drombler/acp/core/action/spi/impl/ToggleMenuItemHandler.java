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
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.drombler.acp.core.action.jaxb.MenusType;
import org.drombler.acp.core.action.jaxb.ToggleMenuEntryType;
import org.drombler.acp.core.action.spi.ActionRegistry;
import org.drombler.acp.core.action.spi.ToggleActionFactory;
import org.drombler.acp.core.action.spi.ToggleMenuEntryDescriptor;
import org.drombler.acp.core.action.spi.ToggleMenuItemFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "toggleMenuEntryDescriptor", referenceInterface = ToggleMenuEntryDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class ToggleMenuItemHandler<MenuItem, Menu extends MenuItem, ToggleMenuItem extends MenuItem, ToggleAction>
        extends AbstractMenuItemHandler<MenuItem, Menu, ToggleMenuItem, ToggleMenuEntryDescriptor, MenuItemConfig<ToggleAction>> {

    @Reference
    private ToggleMenuItemFactory<ToggleMenuItem, ToggleAction> toggleMenuItemFactory;
    @Reference
    private ToggleActionFactory<ToggleAction> toggleActionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();
    private final ActionResolutionManager<ToggleMenuEntryDescriptor> actionResolutionManager = new ActionResolutionManager<>();
    private ServiceTracker<ToggleAction, ServiceReference<ToggleAction>> tracker;

    protected void bindToggleMenuEntryDescriptor(ServiceReference<ToggleMenuEntryDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ToggleMenuEntryDescriptor menuEntryDescriptor = context.getService(serviceReference);
        resolveMenuItem(menuEntryDescriptor, context);
    }

    protected void unbindToggleMenuEntryDescriptor(ServiceReference<ToggleMenuEntryDescriptor> serviceReference) {
        // TODO
    }

    protected void bindToggleMenuItemFactory(ToggleMenuItemFactory<ToggleMenuItem, ToggleAction> toggleMenuItemFactory) {
        this.toggleMenuItemFactory = toggleMenuItemFactory;
    }

    protected void unbindToggleMenuItemFactory(ToggleMenuItemFactory<ToggleMenuItem, ToggleAction> toggleMenuItemFactory) {
        this.toggleMenuItemFactory = null;
    }

    protected void bindToggleActionFactory(ToggleActionFactory<ToggleAction> toggleActionFactory) {
        this.toggleActionFactory = toggleActionFactory;
    }

    protected void unbindToggleActionFactory(ToggleActionFactory<ToggleAction> toggleActionFactory) {
        this.toggleActionFactory = null;
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
                new ServiceTrackerCustomizer<ToggleAction, ServiceReference<ToggleAction>>() {
                    @Override
                    public ServiceReference<ToggleAction> addingService(ServiceReference<ToggleAction> reference) {
                        String actionId = actionRegistry.getActionId(reference);
                        if (actionResolutionManager.containsUnresolvedEntries(actionId)) {
                            for (UnresolvedEntry<ToggleMenuEntryDescriptor> unresolvedEntry :
                                    actionResolutionManager.removeUnresolvedEntries(actionId)) {
                                resolveMenuItem(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
                            }
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
        for (ToggleMenuEntryType menuEntry : menusType.getToggleMenuEntry()) {
            ToggleMenuEntryDescriptor menuEntryDescriptor = ToggleMenuEntryDescriptor.createRadioMenuEntryDescriptor(
                    menuEntry);
            resolveMenuItem(menuEntryDescriptor, context);
        }
    }

    @Override
    protected MenuItemConfig<ToggleAction> createConfig(ToggleMenuEntryDescriptor menuEntryDescriptor,
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
    protected ToggleMenuItem createMenuItem(ToggleMenuEntryDescriptor menuEntryDescriptor,
            MenuItemConfig<ToggleAction> config) {
//        System.out.println(actionFactory.getToggleActionClass().getName() + ": " + menuEntryDescriptor.getActionId());
        return toggleMenuItemFactory.createToggleMenuItem(menuEntryDescriptor, config.getAction(), config.getIconSize());
    }

    @Override
    protected void registerUnresolvedMenuItem(ToggleMenuEntryDescriptor menuEntryDescriptor, BundleContext context) {
        actionResolutionManager.addUnresolvedEntry(menuEntryDescriptor.getActionId(),
                new UnresolvedEntry<>(menuEntryDescriptor, context));
    }
}
