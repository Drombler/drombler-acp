package org.drombler.acp.core.action;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author puce
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <F> the sorting strategy specific menu item supplier factory type
 */
public interface MenuItemSortingStrategy<MenuItem, F extends MenuItemSupplierFactory<MenuItem>> {

    int getMenuItemInsertionPoint(List<? extends MenuItemSupplierFactoryEntry<MenuItem, F>> entryList, MenuItemSupplierFactoryEntry<MenuItem, F> entry);

    Optional<Integer> getSeparatorInsertionPoint(int index, List<? extends MenuItemSupplierFactoryEntry<MenuItem, F>> entryList, MenuItemSupplierFactoryEntry<MenuItem, F> entry);

}
