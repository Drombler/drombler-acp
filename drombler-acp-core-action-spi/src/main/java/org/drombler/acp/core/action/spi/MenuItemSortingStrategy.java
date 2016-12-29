package org.drombler.acp.core.action.spi;

import java.util.List;

public interface MenuItemSortingStrategy<MenuItem, F extends MenuItemSupplierFactory<MenuItem>> {

    int getInsertionPoint(List<? extends MenuItemSupplierFactoryEntry<MenuItem, F>> entryList, MenuItemSupplierFactoryEntry<MenuItem, F> entry);

    MenuItemEntry<MenuItem> createSeparatorEntry(int index, List<? extends MenuItemSupplierFactoryEntry<MenuItem, F>> entryList,
            MenuItemSupplierFactoryEntry<MenuItem, F> entry, SeparatorMenuItemFactory<? extends MenuItem> separatorMenuItemFactory);

}
