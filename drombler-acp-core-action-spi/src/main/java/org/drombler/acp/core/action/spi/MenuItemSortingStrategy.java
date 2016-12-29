package org.drombler.acp.core.action.spi;

import java.util.List;
/**
 *
 * @author puce
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <F> the sorting strategy specific menu item supplier factory type
 */
public interface MenuItemSortingStrategy<MenuItem, F extends MenuItemSupplierFactory<MenuItem>> {

    int getInsertionPoint(List<? extends MenuItemSupplierFactoryEntry<MenuItem, F>> entryList, MenuItemSupplierFactoryEntry<MenuItem, F> entry);

    MenuItemEntry<MenuItem> createSeparatorEntry(int index, List<? extends MenuItemSupplierFactoryEntry<MenuItem, F>> entryList,
            MenuItemSupplierFactoryEntry<MenuItem, F> entry, SeparatorMenuItemFactory<? extends MenuItem> separatorMenuItemFactory);

}
