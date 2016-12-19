package org.drombler.acp.core.action.spi;



// TODO: good name?
public class IdentityMenuItemSupplierFactory<MenuItem> implements MenuItemSupplierFactory<MenuItem> {

    @Override
    public <T extends MenuItem> IdentityMenuItemSupplier<T> createMenuItemSupplier(T menuItem) {
        return new IdentityMenuItemSupplier<>(menuItem);
    }


}
