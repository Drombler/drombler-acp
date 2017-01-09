package org.drombler.acp.core.action;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.softsmithy.lib.util.Positionables;
/**
 * The default position-based sorting strategy for declaratively registered menus and menu items. This sorting strategy will add separators between menu items in different thousand steps.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @author puce
 */
public class PositionSortingStrategy<MenuItem> implements MenuItemSortingStrategy<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> {

    private static final int SEPARATOR_STEPS = 1000;

    /**
     * {@inheritDoc }
     */
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

    /**
     * {@inheritDoc }
     */
    @Override
    public Optional<Integer> getSeparatorInsertionPoint(int index,
            List<? extends MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>>> entryList,
            MenuItemSupplierFactoryEntry<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> entry) {
        List<PositionableMenuItemAdapter<MenuItem>> menuItemAdapters = mapToMenuItemAdapters(entryList);
        PositionableMenuItemAdapter<MenuItem> menuItemAdapter = createMenuItemSupplier(entry);

        if (index < menuItemAdapters.size() - 1
                && ((menuItemAdapters.get(index + 1).getPosition() / SEPARATOR_STEPS) - (menuItemAdapter.getPosition() / SEPARATOR_STEPS)) >= 1
                && !entryList.get(index + 1).isSeparator()) {
            return Optional.of(index + 1);

        }

        if (index > 0
                && ((menuItemAdapter.getPosition() / SEPARATOR_STEPS) - (menuItemAdapters.get(index - 1).getPosition() / SEPARATOR_STEPS)) >= 1
                && !entryList.get(index - 1).isSeparator()) {
            return Optional.of(index);

        }

        return Optional.empty();
    }


}
