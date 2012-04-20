/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.List;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.action.jaxb.MenusType;
import org.richclientplatform.core.action.spi.AbstractMenuEntryDescriptor;
import org.richclientplatform.core.action.spi.MenuBarMenuContainerProvider;
import org.richclientplatform.core.action.spi.MenuItemContainer;
import org.richclientplatform.core.action.spi.MenuItemContainerMenuEvent;
import org.richclientplatform.core.action.spi.MenuItemContainerListenerAdapter;
import org.richclientplatform.core.action.spi.MenuItemRootContainer;
import org.richclientplatform.core.action.spi.PositionableMenuItemAdapter;

/**
 *
 * @author puce
 */
@References({
    @Reference(name = "menusType", referenceInterface = MenusType.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "menuBarMenuContainerProvider", referenceInterface = MenuBarMenuContainerProvider.class)
})
public abstract class AbstractMenuItemHandler<MenuItem, Menu extends MenuItem, M extends MenuItem, D extends AbstractMenuEntryDescriptor> {

    private static final String ROOT_PATH_ID = "";
    private static final int ICON_SIZE = 16;
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

    protected boolean isInitialized() {
        return rootContainer != null;
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

    protected void resolveMenuItem(D menuEntryDescriptor, BundleContext context) {
        if (isInitialized()) {
            M menuItem = createMenuItem(menuEntryDescriptor, context, ICON_SIZE);
            if (menuItem != null) {
                resolveMenuItem(menuEntryDescriptor, context, menuItem);
            } else {
                registerUnresolvedMenuItem(menuEntryDescriptor, context);
            }
        } else {
            registerUnresolvedMenuEntry(menuEntryDescriptor, context);
        }
    }

    protected abstract M createMenuItem(D menuEntryDescriptor, BundleContext context, int iconSize);

    protected abstract void registerUnresolvedMenuItem(D menuEntryDescriptor, BundleContext context);

    private void resolveMenuItem(D menuEntryDescriptor, BundleContext context, M menuItem) {
        MenuItemContainer<MenuItem, Menu> parentContainer = getParent(menuEntryDescriptor.getPath());
        if (parentContainer != null) {
            addToContainer(parentContainer, menuItem, menuEntryDescriptor);
        } else {
            registerUnresolvedMenuEntry(menuEntryDescriptor, context);
        }
    }

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
            for (UnresolvedEntry<D> unresolvedEntry : menuItemResolutionManager.removeUnresolvedMenuEntries(pathId)) {
                resolveMenuItem(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
            }
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
