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
import org.osgi.service.component.ComponentContext;
import org.drombler.acp.core.action.jaxb.MenusType;
import org.drombler.acp.core.action.spi.MenuDescriptor;
import org.drombler.acp.core.action.spi.MenuFactory;
import org.drombler.acp.core.action.spi.MenuItemContainer;
import org.drombler.acp.core.action.spi.PositionableMenuItemAdapter;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "menuDescriptor", referenceInterface = MenuDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class MenuHandler<MenuItem, Menu extends MenuItem> extends AbstractMenuItemHandler<MenuItem, Menu, Menu, MenuDescriptor, MenuConfig> {

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
    }

    protected void unbindMenuFactory(MenuFactory<Menu> menuFactory) {
        this.menuFactory = null;
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedItems();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    private void resolveMenu(MenuDescriptor entry) {
        resolveMenuItem(entry, null); // TODO: good to pass null? better abstraction?
    }

    @Override
    protected void resolveMenuItem(MenusType menusType, Bundle bundle, BundleContext context) {
        menusType.getMenu().stream().
                map(menu -> MenuDescriptor.createMenuDescriptor(menu, bundle)).
                forEach(menuDescriptor -> resolveMenu(menuDescriptor));
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
    protected MenuConfig createConfig(MenuDescriptor menuEntryDescriptor, BundleContext context) {
        return MenuConfig.getInstance();
    }

    @Override
    protected Menu createMenuItem(MenuDescriptor menuEntryDescriptor, MenuConfig config) {
        return menuFactory.createMenu(menuEntryDescriptor);
    }

    @Override
    protected void registerUnresolvedMenuItem(MenuDescriptor menuEntryDescriptor, BundleContext context) {
        // nothing to do
    }
}