package org.drombler.acp.core.action.spi;

/**
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <F> the GUI toolkit and sorting strategy specific menu item supplier factory type
 * @author puce
 */
public class MenuItemSupplierFactoryEntry<MenuItem, F extends MenuItemSupplierFactory<MenuItem>> {

    private final F supplierFactory;
    private final MenuItem menuItem;


    public MenuItemSupplierFactoryEntry(F supplierFactory, MenuItem menuItem) {
        this.supplierFactory = supplierFactory;
        this.menuItem = menuItem;
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


}
