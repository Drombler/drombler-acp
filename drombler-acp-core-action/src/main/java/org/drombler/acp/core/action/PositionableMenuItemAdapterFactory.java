package org.drombler.acp.core.action;

/**
 * This {@link MenuItemSupplierFactory} knows the position to associate with the according menu item. The position is passed to the {@link PositionableMenuItemAdapter} and is needed by the
 * {@link PositionSortingStrategy}.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @see PositionSortingStrategy
 * @author puce
 */
public class PositionableMenuItemAdapterFactory<MenuItem> implements MenuItemSupplierFactory<MenuItem> {

    private final int position;

    /**
     * Creates a new instance of this class.
     *
     * @param position the position to associate with the according menu item.
     */
    public PositionableMenuItemAdapterFactory(int position) {
        this.position = position;
    }
    /**
     * {@inheritDoc }
     */
    @Override
    public <T extends MenuItem> PositionableMenuItemAdapter<T> createMenuItemSupplier(T menuItem) {
        return new PositionableMenuItemAdapter<>(menuItem, position);
    }

}
