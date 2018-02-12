package org.drombler.acp.core.action.spi;

import org.drombler.acp.core.action.MenuItemSortingStrategy;
import org.drombler.acp.core.action.MenuItemSupplierFactory;


/**
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @author puce
 */
public interface MenuMenuItemContainerFactory<MenuItem, Menu extends MenuItem> {

    <F extends MenuItemSupplierFactory<MenuItem, F>> MenuItemContainer<MenuItem, Menu, F> createMenuMenuItemContainer(String id, Menu menu,
            MenuItemContainer<MenuItem, Menu, ?> parentMenuContainer,
            AbstractMenuItemRootContainer<MenuItem, Menu, ?> rootContainer,
            MenuItemSortingStrategy<MenuItem, F> menuItemSortingStrategy,
            MenuMenuItemContainerFactory<MenuItem, Menu> menuMenuItemContainerFactory,
            SeparatorMenuItemFactory<? extends MenuItem> separatorMenuItemFactory);
}
