package org.drombler.acp.core.action;

/**
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @author puce
 */
public interface MenuItemSupplierFactory<MenuItem> {

    <T extends MenuItem> MenuItemSupplier<T> createMenuItemSupplier(T menuItem);
}
