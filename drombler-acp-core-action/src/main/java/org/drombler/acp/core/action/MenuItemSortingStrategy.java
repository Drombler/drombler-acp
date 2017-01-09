package org.drombler.acp.core.action;

import java.util.List;
import java.util.Optional;

/**
 * A custom sorting strategy for programmatically registered menus.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <F> the sorting strategy specific menu item supplier factory type
 * @see PositionSortingStrategy
 * @author puce
 */
public interface MenuItemSortingStrategy<MenuItem, F extends MenuItemSupplierFactory<MenuItem>> {

    /**
     * Gets the instertion point of the new menu entry.
     *
     * @param entryList the existing menu entries
     * @param entry the new entry
     * @return the instertion point of the new menu entry
     */
    int getMenuItemInsertionPoint(List<? extends MenuItemSupplierFactoryEntry<MenuItem, F>> entryList, MenuItemSupplierFactoryEntry<MenuItem, F> entry);

    /**
     * Gets the instertion point of a new separator. Returns empty if no new separator should be added. This method gets called after {@link #getMenuItemInsertionPoint(java.util.List, org.drombler.acp.core.action.MenuItemSupplierFactoryEntry)
     * }.
     *
     * @param index the index of the new menu entry which was added to the list
     * @param entryList the existing menu entries including the new menu entry
     * @param entry the new menu entry which was added to the list
     * @return the instertion point of a new separator or empty if no new separator should be added
     */
    Optional<Integer> getSeparatorInsertionPoint(int index, List<? extends MenuItemSupplierFactoryEntry<MenuItem, F>> entryList, MenuItemSupplierFactoryEntry<MenuItem, F> entry);

}
