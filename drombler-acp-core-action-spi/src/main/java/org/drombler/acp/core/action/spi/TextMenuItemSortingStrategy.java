package org.drombler.acp.core.action.spi;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.softsmithy.lib.util.Lists;


public class TextMenuItemSortingStrategy<MenuItem> implements MenuItemSortingStrategy<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>> {
    private final Collator collator;
    private final Function<MenuItem, String> menuTextExtractor;

    public TextMenuItemSortingStrategy(Function<MenuItem, String> menuTextExtractor) {
        this(menuTextExtractor, Collator.getInstance(Locale.getDefault(Locale.Category.DISPLAY)));
    }

    public TextMenuItemSortingStrategy(Function<MenuItem, String> menuTextExtractor, Collator collator) {
        this.menuTextExtractor = menuTextExtractor;
        this.collator = collator;
    }

    @Override
    public int getInsertionPoint(List<? extends MenuItemSupplierFactoryEntry<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>>> entryList,
            MenuItemSupplierFactoryEntry<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>> entry) {
        List<IdentityMenuItemSupplier<MenuItem>> menuItemAdapters = mapToMenuItemAdapters(entryList);
        IdentityMenuItemSupplier<MenuItem> menuItemAdapter = createMenuItemSupplier(entry);

        return Lists.getInsertionPoint(menuItemAdapters, menuItemAdapter,
                Comparator.comparing(adapter -> menuTextExtractor.apply(adapter.getMenuItem()), collator));
    }

    private List<IdentityMenuItemSupplier<MenuItem>> mapToMenuItemAdapters(List<? extends MenuItemSupplierFactoryEntry<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>>> entryList) {
        return entryList.stream().map(this::createMenuItemSupplier).collect(Collectors.toList());
    }

    private IdentityMenuItemSupplier<MenuItem> createMenuItemSupplier(MenuItemSupplierFactoryEntry<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>> entry) {
        return entry.getSupplierFactory().createMenuItemSupplier(entry.getMenuItem());
    }

    @Override
    public MenuItemEntry<MenuItem> createSeparatorEntry(int index,
            List<? extends MenuItemSupplierFactoryEntry<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>>> entryList,
            MenuItemSupplierFactoryEntry<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>> entry,
            SeparatorMenuItemFactory<? extends MenuItem> separatorMenuItemFactory) {
            return null;
    }

}
