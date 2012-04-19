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
import org.richclientplatform.core.action.jaxb.MenuType;
import org.richclientplatform.core.action.jaxb.MenusType;
import org.richclientplatform.core.action.spi.MenuDescriptor;
import org.richclientplatform.core.action.spi.MenuFactory;
import org.richclientplatform.core.action.spi.MenuItemContainer;
import org.richclientplatform.core.action.spi.PositionableMenuItemAdapter;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "menuDescriptor", referenceInterface = MenuDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class MenuHandler<MenuItem, Menu extends MenuItem> extends AbstractMenuItemHandler<MenuItem, Menu, Menu, MenuDescriptor> {

    @Reference
    private MenuFactory<Menu> menuFactory;

    protected void bindMenuDescriptor(MenuDescriptor menuDescriptor) {
        resolveMenu(menuDescriptor);
    }

    protected void unbindMenuDescriptor(MenuDescriptor menuDescriptor) {
        // TODO
    }

    protected void bindMenuFactory(MenuFactory<Menu> menuFactory) {
        this.menuFactory = menuFactory;
        resolveUnresolvedItems();
    }

    protected void unbindMenuFactory(MenuFactory<Menu> menuFactory) {
        this.menuFactory = null;
    }

    private void resolveMenu(MenuDescriptor entry) {
        resolveMenuItem(entry, null); // TODO: good to pass null? better abstraction?
    }

    @Override
    protected void resolveMenuItem(MenusType menusType, Bundle bundle, BundleContext context) {
        for (MenuType menu : menusType.getMenu()) {
            MenuDescriptor menuDescriptor = MenuDescriptor.createMenuDescriptor(menu, bundle);
            resolveMenu(menuDescriptor);
        }
    }

//    private void resolveMenu(MenuDescriptor menuDescriptor) {
//        MenuItemContainer<Menu, MenuItem> parentContainer = getParent(menuDescriptor.getPath());
//        if (parentContainer != null && isInitialized()) {
//
//            parentContainer.addMenu(menuDescriptor.getId(),
//                    PositionableMenuItemAdapter.wrapMenuItem(menu, menuDescriptor.getPosition()));
//            MenuItemResolutionManager<MenuDescriptor> manager = getMenuItemResolutionManager(menuDescriptor.getPath());
//            resolveUnresolvedItems(manager, menuDescriptor.getId());
//        } else {
//            registerUnresolvedMenuEntry(menuDescriptor, null); // TODO: good to pass null? better abstraction?
//        }
//    }
    @Override
    protected void addToContainer(MenuItemContainer<MenuItem, Menu> parentContainer, Menu menuItem, MenuDescriptor menuDescriptor) {
        parentContainer.addMenu(menuDescriptor.getId(),
                PositionableMenuItemAdapter.wrapMenuItem(menuItem, menuDescriptor.getPosition()));
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && menuFactory != null;
    }


    @Override
    protected Menu createMenuItem(MenuDescriptor menuEntryDescriptor, BundleContext context, int iconSize) {
        return menuFactory.createMenu(menuEntryDescriptor);
    }
}
