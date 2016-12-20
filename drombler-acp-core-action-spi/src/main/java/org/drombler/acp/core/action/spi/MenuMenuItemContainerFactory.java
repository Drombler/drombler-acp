package org.drombler.acp.core.action.spi;


/**
 *
 * @author puce
 */
public interface MenuMenuItemContainerFactory<MenuItem, Menu extends MenuItem> {

    <F extends MenuItemSupplierFactory<MenuItem>> MenuItemContainer<MenuItem, Menu, F> createMenuMenuItemContainer(String id, Menu menu,
            MenuItemContainer<MenuItem, Menu, ?> parentMenuContainer,
            AbstractMenuItemRootContainer<MenuItem, Menu, ?> rootContainer,
            MenuItemSortingStrategy<MenuItem, F> menuItemSortingStrategy,
            MenuMenuItemContainerFactory<MenuItem, Menu> menuMenuItemContainerFactory,
            SeparatorMenuItemFactory<? extends MenuItem> separatorMenuItemFactory);
}
