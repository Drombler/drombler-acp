package org.drombler.acp.core.action;

/**
 * A menu item supplier factory creates {@link MenuItemSupplier}s for the according menu item. Implementations can associate some custom data with this factory which is needed by a custom
 * {@link MenuItemSortingStrategy} and can pass them to the MenuItemSupplier.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @see MenuItemSortingStrategy
 * @author puce
 */
public interface MenuItemSupplierFactory<MenuItem> {

    /**
     * Creates a MenuItemSupplier for the according menu item.
     *
     * @param <T> the type of the menu item
     * @param menuItem the according menu item
     * @return a MenuItemSupplier for a given menu item
     */
    <T extends MenuItem> MenuItemSupplier<T> createMenuItemSupplier(T menuItem);
}
