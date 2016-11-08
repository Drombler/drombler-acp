package org.drombler.acp.core.action.spi;

/**
 *
 * @author puce
 */


public class PositionableMenuItemAdapterFactory<MenuItem> implements MenuItemSupplierFactory<MenuItem> {

    private final int position;
    private final boolean separator;

    public PositionableMenuItemAdapterFactory(int position, boolean separator) {
        this.position = position;
        this.separator = separator;
    }

    @Override
    public <T extends MenuItem> PositionableMenuItemAdapter<T> createMenuItemSupplier(T menuItem) {
        return new PositionableMenuItemAdapter<>(menuItem, position, separator);
    }

}
