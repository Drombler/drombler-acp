package org.drombler.acp.core.action;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.softsmithy.lib.util.Positionables;
/**
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @author puce
 */
public class PositionSortingStrategy<MenuItem> implements MenuItemSortingStrategy<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> {

    private static final int SEPARATOR_STEPS = 1000;

    @Override
    public int getMenuItemInsertionPoint(List<? extends MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>>> entryList,
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


    @Override
    public Optional<Integer> getSeparatorInsertionPoint(int index,
            List<? extends MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>>> entryList,
            MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> entry) {

//    @Override
//    public MenuItemEntry<PositionableMenuItemAdapter<? extends MenuItem>> getSeparatorInsertionPoint(int index,
//            List<? extends PositionableMenuItemAdapter<? extends MenuItem>> menuItemList,
//            PositionableMenuItemAdapter<? extends MenuItem> menuItem) {
        List<PositionableMenuItemAdapter<MenuItem>> menuItemAdapters = mapToMenuItemAdapters(entryList);
        PositionableMenuItemAdapter<MenuItem> menuItemAdapter = createMenuItemSupplier(entry);

        if (index < menuItemAdapters.size() - 1
                && ((menuItemAdapters.get(index + 1).getPosition() / SEPARATOR_STEPS) - (menuItemAdapter.getPosition() / SEPARATOR_STEPS)) >= 1
                && !menuItemAdapters.get(index + 1).isSeparator()) {
//            addSeparator(index + 1, createSeparatorMenuItemSupplier(xMenuItems.get(index + 1).getPosition()));
            return Optional.of(index + 1);

        }

        if (index > 0
                && ((menuItemAdapter.getPosition() / SEPARATOR_STEPS) - (menuItemAdapters.get(index - 1).getPosition() / SEPARATOR_STEPS)) >= 1
                && !menuItemAdapters.get(index - 1).isSeparator()) {
//            addSeparator(index, createSeparatorMenuItemSupplier(menuItemAdapter.getPosition()));
            return Optional.of(index);

        }

        return Optional.empty();
    }


}
