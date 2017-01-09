package org.drombler.acp.core.action;

/**
 * A menu item supplier provides menu items. Implementations can associate some custom data with the menu item needed by a custom {@link MenuItemSortingStrategy}.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @see MenuItemSortingStrategy
 * @author puce
 */
public interface MenuItemSupplier<MenuItem> {

    /**
     * Gets a menu item.
     *
     * TODO: remove T?
     *
     * @param <T> the type of the provided menu item
     * @return a menu item
     */
    <T extends MenuItem> T getMenuItem();
}
