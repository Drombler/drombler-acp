package org.drombler.acp.core.action.spi;

import org.drombler.acp.core.action.MenuItemSortingStrategy;
import org.drombler.acp.core.action.MenuItemSupplierFactory;


/**
 * A factory to create (sub-)menu component based {@link MenuItemContainer}s.<br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <Menu> the GUI toolkit specific type for menus
 * @author puce
 */
public interface MenuMenuItemContainerFactory<MenuItem, Menu extends MenuItem> {

    /**
     * Creates a {@link MenuItemContainer} for the specified menu.
     *
     * @param <F> the {@link MenuItemSupplierFactory} subclass
     * @param id the id of the menu
     * @param menu the menu
     * @param parentMenuContainer the parent menu container
     * @param rootContainer the root container
     * @param menuItemSortingStrategy the sorting strategy used by this container
     * @param menuMenuItemContainerFactory the menu {@link MenuItemContainer} tp create sub-menu menu item containers
     * @param separatorMenuItemFactory the factory to create separator menu items
     * @return a {@link MenuItemContainer} for the specified menu
     */
    <F extends MenuItemSupplierFactory<MenuItem, F>> MenuItemContainer<MenuItem, Menu, F> createMenuMenuItemContainer(String id, Menu menu,
            MenuItemContainer<MenuItem, Menu, ?> parentMenuContainer,
            AbstractMenuItemRootContainer<MenuItem, Menu, ?> rootContainer,
            MenuItemSortingStrategy<MenuItem, F> menuItemSortingStrategy,
            MenuMenuItemContainerFactory<MenuItem, Menu> menuMenuItemContainerFactory,
            SeparatorMenuItemFactory<? extends MenuItem> separatorMenuItemFactory);
}
