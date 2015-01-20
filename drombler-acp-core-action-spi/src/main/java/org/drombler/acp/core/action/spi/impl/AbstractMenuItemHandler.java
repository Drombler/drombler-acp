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

import java.util.List;
import java.util.concurrent.Executor;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.drombler.acp.core.action.jaxb.MenusType;
import org.drombler.acp.core.action.spi.AbstractMenuEntryDescriptor;
import org.drombler.acp.core.action.spi.MenuBarMenuContainerProvider;
import org.drombler.acp.core.action.spi.MenuItemContainer;
import org.drombler.acp.core.action.spi.MenuItemContainerListenerAdapter;
import org.drombler.acp.core.action.spi.MenuItemContainerMenuEvent;
import org.drombler.acp.core.action.spi.MenuItemRootContainer;
import org.drombler.acp.core.action.spi.PositionableMenuItemAdapter;
import org.drombler.acp.core.application.ApplicationExecutorProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 *
 * @author puce
 */
@References({
    @Reference(name = "menusType", referenceInterface = MenusType.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "menuBarMenuContainerProvider", referenceInterface = MenuBarMenuContainerProvider.class),
    @Reference(name = "applicationExecutorProvider", referenceInterface = ApplicationExecutorProvider.class)
})
public abstract class AbstractMenuItemHandler<MenuItem, Menu extends MenuItem, M extends MenuItem, D extends AbstractMenuEntryDescriptor, Config> {

    private static final String ROOT_PATH_ID = "";
    private Executor applicationExecutor;
    private final MenuItemResolutionManager<D> menuItemResolutionManager = new MenuItemResolutionManager<>();
    private MenuItemRootContainer<MenuItem, Menu> rootContainer;

    protected void bindMenusType(ServiceReference<MenusType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        MenusType menusType = context.getService(serviceReference);
        resolveMenuItem(menusType, bundle, context);
    }

    protected void unbindMenusType(MenusType menusType) {
        // TODO
    }

    protected void bindMenuBarMenuContainerProvider(MenuBarMenuContainerProvider<MenuItem, Menu> menuBarMenuContainerProvider) {
        rootContainer = menuBarMenuContainerProvider.getMenuBarMenuContainer();
        rootContainer.addMenuContainerListener(new MenuItemContainerListenerAdapter<MenuItem, Menu>() {

            @Override
            public void menuAdded(MenuItemContainerMenuEvent<MenuItem, Menu> event) {
                MenuItemResolutionManager<D> menuItemResolutionManager = getMenuItemResolutionManager(event.getPath());
                resolveUnresolvedItems(menuItemResolutionManager, event.getMenuId());
            }
        });
        resolveUnresolvedItems();
    }

    protected void unbindMenuBarMenuContainerProvider(MenuBarMenuContainerProvider<MenuItem, Menu> menuBarMenuContainerProvider) {
        rootContainer = null;
    }

    protected void bindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = applicationExecutorProvider.getApplicationExecutor();
    }

    protected void unbindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = null;
    }

    protected boolean isInitialized() {
        return rootContainer != null && applicationExecutor != null;
    }

    protected abstract void resolveMenuItem(MenusType menusType, Bundle bundle, BundleContext context);

    private MenuItemContainer<MenuItem, Menu> getParent(List<String> path) {
        MenuItemContainer<MenuItem, Menu> parentContainer = rootContainer;
        for (String pathId : path) {
            if (parentContainer != null) {
                parentContainer = parentContainer.getMenuContainer(pathId);
            } else {
                break;
            }
        }
        return parentContainer;
    }

    private MenuItemResolutionManager<D> getMenuItemResolutionManager(List<String> path) {
//        MenuResolutionManager manager = menuResolutionManager.getMenuItemResolutionManager(ROOT_PATH_ID);
        MenuItemResolutionManager<D> manager = menuItemResolutionManager;
        for (String pathId : path) {
            manager = manager.getMenuResolutionManager(pathId);
        }
        return manager;
    }

    protected void resolveMenuItem(final D menuEntryDescriptor, final BundleContext context) {
        if (isInitialized()) {
            MenuItemContainer<MenuItem, Menu> parentContainer = getParent(menuEntryDescriptor.getPath());
            if (parentContainer != null) {
                Config config = createConfig(menuEntryDescriptor, context);
                if (config != null) {
                    resolveMenuItem(config, menuEntryDescriptor, parentContainer);
                } else {
                    registerUnresolvedMenuItem(menuEntryDescriptor, context);
                }
            } else {
                registerUnresolvedMenuEntry(menuEntryDescriptor, context);
            }
        } else {
            registerUnresolvedMenuEntry(menuEntryDescriptor, context);
        }
    }

    protected void resolveMenuItem(final Config config, final D menuEntryDescriptor, final MenuItemContainer<MenuItem, Menu> parentContainer) {
        applicationExecutor.execute(() -> {
            M menuItem = createMenuItem(menuEntryDescriptor, config);
            addToContainer(parentContainer, menuItem, menuEntryDescriptor);
        });
    }

    protected abstract Config createConfig(D menuEntryDescriptor, BundleContext context);

    protected abstract M createMenuItem(D menuEntryDescriptor, Config config);

    protected abstract void registerUnresolvedMenuItem(D menuEntryDescriptor, BundleContext context);

    protected void addToContainer(MenuItemContainer<MenuItem, Menu> parentContainer, M menuItem, D menuEntryDescriptor) {
        //        if (parentContainer.isSupportingItems()) {
        parentContainer.addMenuItem(
                PositionableMenuItemAdapter.wrapMenuItem(menuItem, menuEntryDescriptor.getPosition()));
        //        }
    }

    protected void resolveUnresolvedItems() {
        resolveUnresolvedItems(menuItemResolutionManager, ROOT_PATH_ID);
    }

    private void resolveUnresolvedItems(MenuItemResolutionManager<D> menuItemResolutionManager, String pathId) {
        if (menuItemResolutionManager.containsUnresolvedMenuEntries(pathId)) {
            menuItemResolutionManager.removeUnresolvedMenuEntries(pathId).forEach(unresolvedEntry
                    -> resolveMenuItem(unresolvedEntry.getEntry(), unresolvedEntry.getContext()));
        }
    }

    private void registerUnresolvedMenuEntry(D menuEntryDescriptor, BundleContext context) {
        MenuItemResolutionManager<D> resolutionManager = this.menuItemResolutionManager;
        //        MenuItemContainer lastResolvedParentContainer = parentContainer;
        String firstUnresolvedPathId = ROOT_PATH_ID;
        if (isInitialized()) {
            MenuItemContainer<MenuItem, Menu> parentContainer = rootContainer;
            //            resolutionManager = resolutionManager.getMenuItemResolutionManager(firstUnresolvedPathId);
            for (String pathId : menuEntryDescriptor.getPath()) {
                parentContainer = parentContainer.getMenuContainer(pathId);
                if (parentContainer != null) {
                    resolutionManager = resolutionManager.getMenuResolutionManager(pathId);
                } else {
                    firstUnresolvedPathId = pathId;
                    break;
                }
            }
        }
        registerUnresolvedMenuEntry(resolutionManager, firstUnresolvedPathId, menuEntryDescriptor, context);
    }

    private void registerUnresolvedMenuEntry(MenuItemResolutionManager<D> resolutionManager, String firstUnresolvedPathId,
            D menuEntryDescriptor, BundleContext context) {
        resolutionManager.addUnresolvedMenuEntry(firstUnresolvedPathId,
                new UnresolvedEntry<>(menuEntryDescriptor, context));
    }
}
