package org.drombler.acp.core.action;

/**
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <F> the sorting strategy specific menu item supplier factory type
 * @author puce
 */
public class MenuItemSupplierFactoryEntry<MenuItem, F extends MenuItemSupplierFactory<MenuItem>> {

    private final F supplierFactory;
    private final MenuItem menuItem;
    private final boolean separator;


    public MenuItemSupplierFactoryEntry(F supplierFactory, MenuItem menuItem, boolean separator) {
        this.supplierFactory = supplierFactory;
        this.menuItem = menuItem;
        this.separator = separator;
    }

    /**
     * @return the supplierFactory
     */
    public F getSupplierFactory() {
        return supplierFactory;
    }

    /**
     * @return the menuItem
     */
    public MenuItem getMenuItem() {
        return menuItem;
    }

    public MenuItemSupplier<MenuItem> getMenuItemSupplier() {
        return supplierFactory.createMenuItemSupplier(menuItem);
    }

    /**
     * @return the separator
     */
    public boolean isSeparator() {
        return separator;
    }

}
