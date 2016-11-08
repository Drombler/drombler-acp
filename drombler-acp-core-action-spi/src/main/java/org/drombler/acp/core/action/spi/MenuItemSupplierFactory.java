package org.drombler.acp.core.action.spi;

/**
 *
 * @author puce
 */


public interface MenuItemSupplierFactory<MenuItem> {

    <T extends MenuItem> MenuItemSupplier<T> createMenuItemSupplier(T menuItem);
}
