package org.drombler.acp.core.action;

/**
 * This entry associates a {@link MenuItemSupplierFactory} with a menu item.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <F> the sorting strategy specific menu item supplier factory type
 * @see MenuItemSortingStrategy
 * @author puce
 */
public class MenuItemSupplierFactoryEntry<MenuItem, F extends MenuItemSupplierFactory<MenuItem, F>> {

    private final F supplierFactory;
    private final MenuItem menuItem;
    private final boolean separator;

    /**
     * Creates a new instance of this class.
     *
     * @param supplierFactory the MenuItemSupplierFactory
     * @param menuItem the menu item
     * @param separator true, if the provided menu item is a separator, else false
     */
    public MenuItemSupplierFactoryEntry(F supplierFactory, MenuItem menuItem, boolean separator) {
        this.supplierFactory = supplierFactory;
        this.menuItem = menuItem;
        this.separator = separator;
    }

    /**
     * Gets the {@link MenuItemSupplierFactory}.
     *
     * @return the MenuItemSupplierFactory
     */
    public F getSupplierFactory() {
        return supplierFactory;
    }

    /**
     * Gets the menu item-
     *
     * @return the menu item
     */
    public MenuItem getMenuItem() {
        return menuItem;
    }

    /**
     * Gets the {@link MenuItemSupplier}.
     *
     * @return the MenuItemSupplier
     */
    public MenuItemSupplier<MenuItem> getMenuItemSupplier() {
        return supplierFactory.createMenuItemSupplier(menuItem);
    }

    /**
     * Indicates if the menu item is a separator or not.
     *
     * @return true, if the provided menu item is a separator, else false
     */
    public boolean isSeparator() {
        return separator;
    }

}
