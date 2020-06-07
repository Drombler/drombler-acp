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
import org.drombler.acp.core.action.MenuItemSupplierFactory;
import org.drombler.acp.core.action.jaxb.MenusType;
import org.drombler.acp.core.action.spi.AbstractMenuEntryDescriptor;
import org.drombler.acp.core.action.spi.MenuBarMenuContainerProvider;
import org.drombler.acp.core.action.spi.MenuItemContainer;
import org.drombler.acp.core.action.spi.MenuItemContainerListenerAdapter;
import org.drombler.acp.core.action.spi.MenuItemContainerMenuEvent;
import org.drombler.acp.core.action.spi.MenuItemRootContainer;
import org.drombler.acp.core.commons.util.UnresolvedEntry;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 *
 * @author puce
 */
public abstract class AbstractMenuItemHandler<MenuItem, Menu extends MenuItem, M extends MenuItem, D extends AbstractMenuEntryDescriptor<MenuItem, ?>, Config> {

    private static final String ROOT_PATH_ID = "";
    private Executor applicationExecutor;
    private final MenuItemResolutionManager<MenuItem, D> menuItemResolutionManager = new MenuItemResolutionManager<>();
    private MenuItemRootContainer<MenuItem, Menu, ?> rootContainer;

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindMenusType(ServiceReference<MenusType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        MenusType menusType = context.getService(serviceReference);
        resolveMenuItem(menusType, bundle, context);
    }

    protected void unbindMenusType(MenusType menusType) {
        // TODO
    }

    @Reference
    protected void bindMenuBarMenuContainerProvider(MenuBarMenuContainerProvider<MenuItem, Menu> menuBarMenuContainerProvider) {
        rootContainer = menuBarMenuContainerProvider.getMenuBarMenuContainer();
        rootContainer.addMenuContainerListener(new MenuItemContainerListenerAdapter<MenuItem, Menu>() {

            @Override
            public void menuAdded(MenuItemContainerMenuEvent<MenuItem, Menu> event) {
                MenuItemResolutionManager<MenuItem, D> menuItemResolutionManager = getMenuItemResolutionManager(event.getPath());
                resolveUnresolvedItems(menuItemResolutionManager, event.getMenuId());
            }
        });
        resolveUnresolvedItems();
    }

    protected void unbindMenuBarMenuContainerProvider(MenuBarMenuContainerProvider<MenuItem, Menu> menuBarMenuContainerProvider) {
        rootContainer = null;
    }

    @Reference
    protected void bindApplicationThreadExecutorProvider(ApplicationThreadExecutorProvider applicationExecutorProvider) {
        applicationExecutor = applicationExecutorProvider.getApplicationThreadExecutor();
    }

    protected void unbindApplicationThreadExecutorProvider(ApplicationThreadExecutorProvider applicationExecutorProvider) {
        applicationExecutor = null;
    }

    protected boolean isInitialized() {
        return rootContainer != null && applicationExecutor != null;
    }

    protected abstract void resolveMenuItem(MenusType menusType, Bundle bundle, BundleContext context);

    private MenuItemContainer<MenuItem, Menu, ?> getParent(List<String> path) {
        MenuItemContainer<MenuItem, Menu, ?> parentContainer = rootContainer;
        for (String pathId : path) {
            if (parentContainer != null) {
                parentContainer = parentContainer.getMenuContainer(pathId);
            } else {
                break;
            }
        }
        return parentContainer;
    }

    private MenuItemResolutionManager<MenuItem, D> getMenuItemResolutionManager(List<String> path) {
//        MenuResolutionManager manager = menuResolutionManager.getMenuItemResolutionManager(ROOT_PATH_ID);
        MenuItemResolutionManager<MenuItem, D> manager = menuItemResolutionManager;
        for (String pathId : path) {
            manager = manager.getMenuResolutionManager(pathId);
        }
        return manager;
    }

    protected void resolveMenuItem(D menuEntryDescriptor, final BundleContext context) {
        if (isInitialized()) {
            MenuItemContainer<MenuItem, Menu, ?> parentContainer = getParent(menuEntryDescriptor.getPath());
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

    protected void resolveMenuItem(Config config, D menuEntryDescriptor, MenuItemContainer<MenuItem, Menu, ?> parentContainer) {
        applicationExecutor.execute(() -> {
            M menuItem = createMenuItem(menuEntryDescriptor, config);
            addToContainer(parentContainer, menuItem, menuEntryDescriptor);
        });
    }

    protected abstract Config createConfig(D menuEntryDescriptor, BundleContext context);

    protected abstract M createMenuItem(D menuEntryDescriptor, Config config);

    protected abstract void registerUnresolvedMenuItem(D menuEntryDescriptor, BundleContext context);

    protected <F extends MenuItemSupplierFactory<MenuItem, F>> void addToContainer(MenuItemContainer<MenuItem, Menu, F> parentContainer, M menuItem,
            D menuEntryDescriptor) {
        //        if (parentContainer.isSupportingItems()) {
        parentContainer.addMenuItem(menuItem, (F) menuEntryDescriptor.getMenuItemSupplierFactory());
        //        }
    }

    protected void resolveUnresolvedItems() {
        resolveUnresolvedItems(menuItemResolutionManager, ROOT_PATH_ID);
    }

    private void resolveUnresolvedItems(MenuItemResolutionManager<MenuItem, D> menuItemResolutionManager, String pathId) {
        if (menuItemResolutionManager.containsUnresolvedMenuEntries(pathId)) {
            menuItemResolutionManager.removeUnresolvedMenuEntries(pathId).forEach(unresolvedEntry
                    -> resolveMenuItem(unresolvedEntry.getEntry(), unresolvedEntry.getContext()));
        }
    }

    private void registerUnresolvedMenuEntry(D menuEntryDescriptor, BundleContext context) {
        MenuItemResolutionManager<MenuItem, D> resolutionManager = this.menuItemResolutionManager;
        //        MenuItemContainer lastResolvedParentContainer = parentContainer;
        String firstUnresolvedPathId = ROOT_PATH_ID;
        if (isInitialized()) {
            MenuItemContainer<MenuItem, Menu, ?> parentContainer = rootContainer;
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

    private void registerUnresolvedMenuEntry(
            MenuItemResolutionManager<MenuItem, D> resolutionManager, String firstUnresolvedPathId,
            D menuEntryDescriptor, BundleContext context) {
        resolutionManager.addUnresolvedMenuEntry(firstUnresolvedPathId, new UnresolvedEntry<>(menuEntryDescriptor, context));
    }
}
