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
package org.drombler.acp.core.action.spi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.drombler.acp.core.action.MenuItemSortingStrategy;
import org.drombler.acp.core.action.MenuItemSupplier;
import org.drombler.acp.core.action.MenuItemSupplierFactory;
import org.drombler.acp.core.action.MenuItemSupplierFactoryEntry;

/**
 * An abstract base class for {@link MenuItemContainer}s.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @param <F> the sorting strategy specific menu item supplier factory type
 * @see MenuItemSortingStrategy
 * @author puce
 */
public abstract class AbstractMenuItemContainer<MenuItem, Menu extends MenuItem, F extends MenuItemSupplierFactory<MenuItem>> implements MenuItemContainer<MenuItem, Menu, F> {

    private final Map<String, MenuItemContainer<MenuItem, Menu, ?>> menuContainers = new HashMap<>();
    private final String id;
    private final boolean supportingItems;
    private final List<MenuItemSupplierFactoryEntry<MenuItem, F>> xMenuItems = new ArrayList<>();
//    private final Map<String, List<PositionableMenuItemAdapter<?>>> unresolvedMenus = new HashMap<>();
    private final MenuItemContainer<MenuItem, Menu, ?> parentMenuContainer;
    private final MenuItemSortingStrategy<MenuItem, F> menuItemSortingStrategy;
    private final MenuMenuItemContainerFactory<MenuItem, Menu> menuMenuItemContainerFactory;
    private final SeparatorMenuItemFactory<? extends MenuItem> separatorMenuItemFactory;

    public AbstractMenuItemContainer(String id, boolean supportingItems, MenuItemContainer<MenuItem, Menu, ?> parentMenuContainer,
            MenuItemSortingStrategy<MenuItem, F> menuItemSortingStrategy,
            MenuMenuItemContainerFactory<MenuItem, Menu> menuMenuItemContainerFactory,
            SeparatorMenuItemFactory<? extends MenuItem> separatorMenuItemFactory) {
        this.id = id;
        this.supportingItems = supportingItems;
        this.parentMenuContainer = parentMenuContainer;
        this.menuItemSortingStrategy = menuItemSortingStrategy;
        this.menuMenuItemContainerFactory = menuMenuItemContainerFactory;
        this.separatorMenuItemFactory = separatorMenuItemFactory;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MenuItemContainer<MenuItem, Menu, ?> getParentMenuContainer() {
        return parentMenuContainer;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public MenuItemContainer<MenuItem, Menu, ?> getMenuContainer(String id) {
        return menuContainers.get(id);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addMenu(String id, Menu menu, F supplierFactory, MenuItemSortingStrategy<MenuItem, ?> sortingStrategy) {
        MenuItemContainer<MenuItem, Menu, ?> menuMenuItemContainer = menuMenuItemContainerFactory.createMenuMenuItemContainer(id, menu, this,
                getMenuItemRootContainer(), sortingStrategy, menuMenuItemContainerFactory, separatorMenuItemFactory);
        menuContainers.put(id, menuMenuItemContainer);

        addMenuItem(menu, supplierFactory, getMenus());
        fireMenuAddedEvent(supplierFactory.createMenuItemSupplier(menu), id, menuMenuItemContainer);
    }

    private void fireMenuAddedEvent(MenuItemSupplier<? extends Menu> menuSupplier, String id, MenuItemContainer<MenuItem, Menu, ?> menuMenuContainer) {
        getMenuItemRootContainer().fireMenuAddedEvent(menuSupplier, id, menuMenuContainer.getPath());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getPath() {
        List<String> path;
        if (parentMenuContainer != null) {
            // recursion
            path = parentMenuContainer.getPath();
            if (parentMenuContainer.getId() != null) {
                path.add(parentMenuContainer.getId());
            }
        } else {
            path = new ArrayList<>();
        }
        return path;
    }

    private <T extends MenuItem> void addMenuItem(T menuItem, F supplierFactory, List<? super T> menuItemList) {
        MenuItemSupplierFactoryEntry<MenuItem, F> entry = new MenuItemSupplierFactoryEntry<>(supplierFactory, menuItem, false);
        int index = menuItemSortingStrategy.getMenuItemInsertionPoint(xMenuItems, entry);

        addMenuItem(index, menuItem, entry, menuItemList);

        Optional<Integer> separatorInsertionPoint = menuItemSortingStrategy.getSeparatorInsertionPoint(index, xMenuItems, entry);
        if (separatorInsertionPoint != null && separatorInsertionPoint.isPresent()) {
            addSeparator(separatorInsertionPoint.get(), separatorMenuItemFactory.createSeparatorMenuItem(), supplierFactory);
        }

    }

    private <T extends MenuItem> void addMenuItem(int index, T menuItem, MenuItemSupplierFactoryEntry<MenuItem, F> entry, List<? super T> menuItemList) {
        xMenuItems.add(index, entry);
        menuItemList.add(index, menuItem);
    }

    private void fireMenuItemAddedEvent(MenuItemSupplier<? extends MenuItem> menuItemSupplier) {
        getMenuItemRootContainer().fireMenuItemAddedEvent(menuItemSupplier, getPath());
    }

    private void addSeparator(int index, MenuItem separatorMenuItem, F supplierFactory) {
        if (isSupportingItems()) {
            addMenuItem(index, separatorMenuItem, new MenuItemSupplierFactoryEntry<>(supplierFactory, separatorMenuItem, true), getItems());
            fireMenuItemAddedEvent(supplierFactory.createMenuItemSupplier(separatorMenuItem));
        }
    }

    protected abstract List<? super Menu> getMenus();

    /**
     * {@inheritDoc }
     */
    @Override
    public void addMenuItem(MenuItem menuItem, F supplierFactory) {
        addMenuItem(menuItem, supplierFactory, getItems());
        fireMenuItemAddedEvent(supplierFactory.createMenuItemSupplier(menuItem));
    }

    protected abstract List<MenuItem> getItems();

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isSupportingItems() {
        return supportingItems;
    }

    protected abstract AbstractMenuItemRootContainer<MenuItem, Menu, ?> getMenuItemRootContainer();

    /**
     * {@inheritDoc }
     */
    @Override
    public MenuItemSortingStrategy<MenuItem, F> getMenuItemSortingStrategy() {
        return menuItemSortingStrategy;
    }
}
