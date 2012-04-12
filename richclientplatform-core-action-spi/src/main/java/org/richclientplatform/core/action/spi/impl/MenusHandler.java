/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.List;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.osgi.framework.*;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.action.jaxb.MenuEntryType;
import org.richclientplatform.core.action.jaxb.MenuType;
import org.richclientplatform.core.action.jaxb.MenusType;
import org.richclientplatform.core.action.spi.ActionFactory;
import org.richclientplatform.core.action.spi.ActionRegistry;
import org.richclientplatform.core.action.spi.MenuBarMenuContainerProvider;
import org.richclientplatform.core.action.spi.MenuDescriptor;
import org.richclientplatform.core.action.spi.MenuEntryDescriptor;
import org.richclientplatform.core.action.spi.MenuFactory;
import org.richclientplatform.core.action.spi.MenuItemContainer;
import org.richclientplatform.core.action.spi.MenuItemFactory;
import org.richclientplatform.core.action.spi.PositionableMenuItemAdapter;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "menusType", referenceInterface = MenusType.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "menuDescriptor", referenceInterface = MenuDescriptor.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "menuEntryDescriptor", referenceInterface = MenuEntryDescriptor.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "menuBarMenuContainerProvider", referenceInterface = MenuBarMenuContainerProvider.class)
})
public class MenusHandler<M, I, A> {

    private static final String ROOT_PATH_ID = "";
    private static final int ICON_SIZE = 16;
    private final MenuResolutionManager menuResolutionManager = new MenuResolutionManager();
    private MenuItemContainer<M, I> rootContainer;
    @Reference
    private MenuFactory<M> menuFactory;
    @Reference
    private MenuItemFactory<I, A> menuItemFactory;
    @Reference
    private ActionFactory<A> actionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();

    protected void bindMenusType(ServiceReference<MenusType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        MenusType menusType = context.getService(serviceReference);

        for (MenuType menu : menusType.getMenu()) {
            MenuDescriptor menuDescriptor = MenuDescriptor.createMenuDescriptor(menu, bundle);
            resolveMenu(menuDescriptor);
        }

        for (MenuEntryType menuEntry : menusType.getMenuEntry()) {
            MenuEntryDescriptor menuEntryDescriptor = MenuEntryDescriptor.createMenuEntryDescriptor(menuEntry);
            resolveMenuItem(menuEntryDescriptor, context);
        }

    }

    protected void unbindMenusType(MenusType menusType) {
        // TODO
    }

    protected void bindMenuDescriptor(MenuDescriptor menuDescriptor) {
        resolveMenu(menuDescriptor);
    }

    protected void unbindMenuDescriptor(MenuDescriptor menuDescriptor) {
        // TODO
    }

    protected void bindMenuEntryDescriptor(ServiceReference<MenuEntryDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        MenuEntryDescriptor menuEntryDescriptor = context.getService(serviceReference);
        resolveMenuItem(menuEntryDescriptor, context);
    }

    protected void unbindMenuEntryDescriptor(ServiceReference<MenuEntryDescriptor> serviceReference) {
        // TODO
    }

    protected void bindMenuBarMenuContainerProvider(MenuBarMenuContainerProvider<M, I> menuBarMenuContainerProvider) {
        rootContainer = menuBarMenuContainerProvider.getMenuBarMenuContainer();
        resolveUnresolvedItems(menuResolutionManager, ROOT_PATH_ID);
    }

    protected void unbindMenuBarMenuContainerProvider(MenuBarMenuContainerProvider<M, I> menuBarMenuContainerProvider) {
        rootContainer = null;
    }

    protected void bindMenuFactory(MenuFactory<M> menuFactory) {
        this.menuFactory = menuFactory;
        resolveUnresolvedItems(menuResolutionManager, ROOT_PATH_ID);
    }

    protected void unbindMenuFactory(MenuFactory<M> menuFactory) {
        this.menuFactory = null;
    }

    protected void bindMenuItemFactory(MenuItemFactory<I, A> menuItemFactory) {
        this.menuItemFactory = menuItemFactory;
        resolveUnresolvedItems(menuResolutionManager, ROOT_PATH_ID);
    }

    protected void unbindMenuItemFactory(MenuItemFactory<I, A> menuItemFactory) {
        this.menuItemFactory = null;
    }

    protected void bindActionFactory(ActionFactory<A> actionFactory) {
        this.actionFactory = actionFactory;
        resolveUnresolvedItems(menuResolutionManager, ROOT_PATH_ID);
    }

    protected void unbindActionFactory(ActionFactory<A> actionFactory) {
        this.actionFactory = null;
    }

    private void resolveMenu(MenuDescriptor menuDescriptor) {
        MenuItemContainer<M, I> parentContainer = getParent(menuDescriptor.getPath());
        if (parentContainer != null && isInitialized()) {
            M menu = menuFactory.createMenu(menuDescriptor);
            parentContainer.addMenu(menuDescriptor.getId(),
                    PositionableMenuItemAdapter.wrapMenuItem(menu, menuDescriptor.getPosition()));
            MenuResolutionManager manager = getMenuResolutionManager(menuDescriptor.getPath());
            resolveUnresolvedItems(manager, menuDescriptor.getId());
        } else {
            registerUnresolvedMenu(menuDescriptor);
        }
    }

    private boolean isInitialized() {
        return rootContainer != null && menuFactory != null && menuItemFactory != null && actionFactory != null;
    }

    private void resolveMenuItem(MenuEntryDescriptor menuEntryDescriptor, BundleContext context) {
        MenuItemContainer<M, I> parentContainer = getParent(menuEntryDescriptor.getPath());
        if (parentContainer != null && isInitialized()) {
            A action = actionRegistry.getAction(menuEntryDescriptor.getActionId(), actionFactory.getActionClass(),
                    context);
            if (action != null) {
                I menuItem = menuItemFactory.createMenuItem(menuEntryDescriptor, action, ICON_SIZE);
//        if (parentContainer.isSupportingItems()) {
                parentContainer.addMenuItem(PositionableMenuItemAdapter.wrapMenuItem(menuItem,
                        menuEntryDescriptor.getPosition()));
            }
//        }
        } else {
            registerUnresolvedMenuEntry(menuEntryDescriptor, context);
        }
    }

    private MenuItemContainer<M, I> getParent(List<String> path) {
        MenuItemContainer<M, I> parentContainer = rootContainer;
        for (String pathId : path) {
            if (parentContainer != null) {
                parentContainer = parentContainer.getMenuContainer(pathId);
            } else {
                break;
            }
        }
        return parentContainer;
    }

    private void registerUnresolvedMenu(MenuDescriptor menuDescriptor) {
        MenuResolutionManager resolutionManager = this.menuResolutionManager;
//        MenuItemContainer lastResolvedParentContainer = parentContainer;
        String firstUnresolvedPathId = ROOT_PATH_ID;
        if (isInitialized()) {
            MenuItemContainer<M, I> parentContainer = rootContainer;
//            resolutionManager = resolutionManager.getMenuResolutionManager(firstUnresolvedPathId);
            for (String pathId : menuDescriptor.getPath()) {
                parentContainer = parentContainer.getMenuContainer(pathId);
                if (parentContainer != null) {
                    resolutionManager = resolutionManager.getMenuResolutionManager(pathId);
                } else {
                    firstUnresolvedPathId = pathId;
                    break;
                }
            }
        }
        resolutionManager.addUnresolvedMenu(firstUnresolvedPathId, menuDescriptor);
    }

    private void resolveUnresolvedItems(MenuResolutionManager container, String pathId) {
        if (container.containsUnresolvedMenus(pathId)) {
            for (MenuDescriptor menuDescriptor : container.removeUnresolvedMenus(pathId)) {
                resolveMenu(menuDescriptor);
            }
        }
        if (container.containsUnresolvedMenuEntries(pathId)) {
            for (UnresolvedMenuEntry unresolvedMenuEntry : container.removeUnresolvedMenuEntries(pathId)) {
                resolveMenuItem(unresolvedMenuEntry.getMenuEntryDescriptor(), unresolvedMenuEntry.getContext());
            }
        }
    }

    private MenuResolutionManager getMenuResolutionManager(List<String> path) {
//        MenuResolutionManager manager = menuResolutionManager.getMenuResolutionManager(ROOT_PATH_ID);
        MenuResolutionManager manager = menuResolutionManager;
        for (String pathId : path) {
            manager = manager.getMenuResolutionManager(pathId);
        }
        return manager;
    }

    private void registerUnresolvedMenuEntry(MenuEntryDescriptor menuEntryDescriptor, BundleContext context) {
        MenuResolutionManager resolutionManager = this.menuResolutionManager;
//        MenuItemContainer lastResolvedParentContainer = parentContainer;
        String firstUnresolvedPathId = ROOT_PATH_ID;
        if (isInitialized()) {
            MenuItemContainer<M, I> parentContainer = rootContainer;
//            resolutionManager = resolutionManager.getMenuResolutionManager(firstUnresolvedPathId);
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
        resolutionManager.addUnresolvedMenuEntry(firstUnresolvedPathId, new UnresolvedMenuEntry(menuEntryDescriptor,
                context));
    }
}
