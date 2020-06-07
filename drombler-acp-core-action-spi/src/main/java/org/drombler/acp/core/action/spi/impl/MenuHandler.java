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

import org.drombler.acp.core.action.MenuItemSupplierFactory;
import org.drombler.acp.core.action.jaxb.MenusType;
import org.drombler.acp.core.action.spi.MenuDescriptor;
import org.drombler.acp.core.action.spi.MenuFactory;
import org.drombler.acp.core.action.spi.MenuItemContainer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 *
 * @author puce
 */
@Component(immediate = true)
public class MenuHandler<MenuItem, Menu extends MenuItem> extends AbstractMenuItemHandler<MenuItem, Menu, Menu, MenuDescriptor<MenuItem, ?>, MenuConfig> {

    @Reference
    private MenuFactory<Menu> menuFactory;

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindMenuDescriptor(MenuDescriptor<MenuItem, ?> menuDescriptor) {
        resolveMenu(menuDescriptor);
    }

    protected void unbindMenuDescriptor(MenuDescriptor<MenuItem, ?> menuDescriptor) {
        // TODO
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedItems();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    private void resolveMenu(MenuDescriptor<MenuItem, ?> entry) {
        resolveMenuItem(entry, null); // TODO: good to pass null? better abstraction?
    }

    @Override
    protected void resolveMenuItem(MenusType menusType, Bundle bundle, BundleContext context) {
        menusType.getMenu().stream().
                map(menu -> MenuDescriptor.<MenuItem>createMenuDescriptor(menu, bundle)).
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
    protected <F extends MenuItemSupplierFactory<MenuItem, F>> void addToContainer(MenuItemContainer<MenuItem, Menu, F> parentContainer, Menu menu, MenuDescriptor<MenuItem, ?> menuDescriptor) {
        parentContainer.addMenu(menuDescriptor.getId(), menu, (F) menuDescriptor.getMenuItemSupplierFactory(), menuDescriptor.getSortingStrategy());
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && menuFactory != null;
    }

    @Override
    protected MenuConfig createConfig(MenuDescriptor<MenuItem, ?> menuEntryDescriptor, BundleContext context) {
        return MenuConfig.getInstance();
    }

    @Override
    protected Menu createMenuItem(MenuDescriptor<MenuItem, ?> menuEntryDescriptor, MenuConfig config) {
        return menuFactory.createMenu(menuEntryDescriptor);
    }

    @Override
    protected void registerUnresolvedMenuItem(MenuDescriptor<MenuItem, ?> menuEntryDescriptor, BundleContext context) {
        // nothing to do
    }
}
