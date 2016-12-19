package org.drombler.acp.core.action.spi;

import java.util.List;
import java.util.stream.Collectors;
import org.softsmithy.lib.util.Positionables;

public class PositionSortingStrategy<MenuItem> implements MenuItemSortingStrategy<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> {

    private static final int SEPARATOR_STEPS = 1000;

    @Override
    public int getInsertionPoint(List<? extends MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>>> entryList,
            MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> entry) {
        List<PositionableMenuItemAdapter<MenuItem>> menuItemAdapters = mapToMenuItemAdapters(entryList);
        PositionableMenuItemAdapter<MenuItem> menuItemAdapter = createMenuItemSupplier(entry);

        return Positionables.getInsertionPoint(menuItemAdapters, menuItemAdapter);
    }

    private List<PositionableMenuItemAdapter<MenuItem>> mapToMenuItemAdapters(List<? extends MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>>> entryList) {
        return entryList.stream().map(this::createMenuItemSupplier).collect(Collectors.toList());
    }

    private PositionableMenuItemAdapter<MenuItem> createMenuItemSupplier(MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> entry) {
        return entry.getSupplierFactory().createMenuItemSupplier(entry.getMenuItem());
    }

    private MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> createSeparatorMenuItemSupplier(int position,
            SeparatorMenuItemFactory<? extends MenuItem> separatorMenuItemFactory) {
        PositionableMenuItemAdapterFactory<MenuItem> positionableMenuItemAdapterFactory = new PositionableMenuItemAdapterFactory<>(position / SEPARATOR_STEPS * SEPARATOR_STEPS, true);
        return new MenuItemSupplierFactoryEntry<>(positionableMenuItemAdapterFactory, separatorMenuItemFactory.createSeparatorMenuItem());
    }

    @Override
    public MenuItemEntry<MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>>> createSeparatorEntry(int index,
            List<? extends MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>>> entryList,
            MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> entry,
            SeparatorMenuItemFactory<? extends MenuItem> separatorMenuItemFactory) {

//    @Override
//    public MenuItemEntry<PositionableMenuItemAdapter<? extends MenuItem>> createSeparatorEntry(int index,
//            List<? extends PositionableMenuItemAdapter<? extends MenuItem>> menuItemList,
//            PositionableMenuItemAdapter<? extends MenuItem> menuItem) {
        List<PositionableMenuItemAdapter<MenuItem>> menuItemAdapters = mapToMenuItemAdapters(entryList);
        PositionableMenuItemAdapter<MenuItem> menuItemAdapter = createMenuItemSupplier(entry);

        if (index < menuItemAdapters.size() - 1
                && ((menuItemAdapters.get(index + 1).getPosition() / SEPARATOR_STEPS) - (menuItemAdapter.getPosition() / SEPARATOR_STEPS)) >= 1
                && !menuItemAdapters.get(index + 1).isSeparator()) {
//            addSeparator(index + 1, createSeparatorMenuItemSupplier(xMenuItems.get(index + 1).getPosition()));
            return new MenuItemEntry<>(index + 1, createSeparatorMenuItemSupplier(menuItemAdapters.get(index + 1).getPosition(), separatorMenuItemFactory));

        }

        if (index > 0
                && ((menuItemAdapter.getPosition() / SEPARATOR_STEPS) - (menuItemAdapters.get(index - 1).getPosition() / SEPARATOR_STEPS)) >= 1
                && !menuItemAdapters.get(index - 1).isSeparator()) {
//            addSeparator(index, createSeparatorMenuItemSupplier(menuItemAdapter.getPosition()));
            return new MenuItemEntry<>(index, createSeparatorMenuItemSupplier(menuItemAdapter.getPosition(), separatorMenuItemFactory));

        }

        return null;
    }


}
