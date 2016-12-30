package org.drombler.acp.core.action;

/**
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @author puce
 */
public class PositionableMenuItemAdapterFactory<MenuItem> implements MenuItemSupplierFactory<MenuItem> {

    private final int position;

    public PositionableMenuItemAdapterFactory(int position) {
        this.position = position;
    }

    @Override
    public <T extends MenuItem> PositionableMenuItemAdapter<T> createMenuItemSupplier(T menuItem) {
        return new PositionableMenuItemAdapter<>(menuItem, position);
    }

}
