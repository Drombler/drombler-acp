package org.drombler.acp.core.action;


/**
 * This simple {@link MenuItemSupplierFactory} creates instances of {@link IdentityMenuItemSupplier}s. No additional data is maintained.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @see MenuItemSortingStrategy
 * @see TextMenuItemSortingStrategy
 * @author puce
 */
// TODO: good name?
public class IdentityMenuItemSupplierFactory<MenuItem> implements MenuItemSupplierFactory<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>> {

    /**
     * {@inheritDoc }
     */
    @Override
    public <T extends MenuItem> IdentityMenuItemSupplier<T> createMenuItemSupplier(T menuItem) {
        return new IdentityMenuItemSupplier<>(menuItem);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public IdentityMenuItemSupplierFactory<MenuItem> toPreviousSeparatorSupplierFactory() {
        return this;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public IdentityMenuItemSupplierFactory<MenuItem> toNextSeparatorSupplierFactory() {
        return this;
    }

}
