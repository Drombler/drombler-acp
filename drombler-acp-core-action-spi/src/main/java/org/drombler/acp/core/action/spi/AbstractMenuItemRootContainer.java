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
import java.util.Collections;
import java.util.List;

/**
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @param <F> the sorting strategy specific menu item supplier factory type
 * @author puce
 */
public abstract class AbstractMenuItemRootContainer<MenuItem, Menu extends MenuItem, F extends MenuItemSupplierFactory<MenuItem>> extends AbstractMenuItemContainer<MenuItem, Menu, F> implements
        MenuItemRootContainer<MenuItem, Menu, F> {

    private final List<MenuItemContainerListener<MenuItem, Menu>> containerListeners
            = Collections.synchronizedList(new ArrayList<>()); // TODO: synchronized needed?

    public AbstractMenuItemRootContainer(boolean supportingItems, MenuItemSortingStrategy menuItemSortingStrategy, MenuMenuItemContainerFactory<MenuItem, Menu> menuMenuItemContainerFactory,
            SeparatorMenuItemFactory<? extends MenuItem> separatorMenuItemFactory) {
        super(null, supportingItems, null, menuItemSortingStrategy, menuMenuItemContainerFactory, separatorMenuItemFactory);
    }

    @Override
    public void addMenuContainerListener(MenuItemContainerListener<MenuItem, Menu> containerListener) {
        containerListeners.add(containerListener);
    }

    @Override
    public void removeMenuContainerListener(MenuItemContainerListener<MenuItem, Menu> containerListener) {
        containerListeners.remove(containerListener);
    }

    void fireMenuAddedEvent(MenuItemSupplier<? extends Menu> menuSupplier, String id, List<String> path) {
        MenuItemContainerMenuEvent<MenuItem, Menu> event = new MenuItemContainerMenuEvent<>(getMenuItemRootContainer(),
                menuSupplier, id, path);
        containerListeners.forEach(containerListener -> containerListener.menuAdded(event));
    }

    void fireMenuItemAddedEvent(MenuItemSupplier<? extends MenuItem> menuItemSupplier, List<String> path) {
        MenuItemContainerMenuItemEvent<MenuItem, Menu> event = new MenuItemContainerMenuItemEvent<>(
                getMenuItemRootContainer(), menuItemSupplier, path);
        containerListeners.stream().forEach((containerListener) -> containerListener.menuItemAdded(event));
    }

    @Override
    protected AbstractMenuItemRootContainer<MenuItem, Menu, F> getMenuItemRootContainer() {
        return this;
    }
}
