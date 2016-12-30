package org.drombler.acp.core.action;


/**
 * TODO: good name?
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @author puce
 */
public class IdentityMenuItemSupplierFactory<MenuItem> implements MenuItemSupplierFactory<MenuItem> {

    @Override
    public <T extends MenuItem> IdentityMenuItemSupplier<T> createMenuItemSupplier(T menuItem) {
        return new IdentityMenuItemSupplier<>(menuItem);
    }


}
