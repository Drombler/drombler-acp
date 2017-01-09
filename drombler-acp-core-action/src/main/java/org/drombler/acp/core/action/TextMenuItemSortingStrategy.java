package org.drombler.acp.core.action;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.softsmithy.lib.util.Lists;

/**
 * A {@link MenuItemSortingStrategy} which sorts the menu items depending on their text using a locale-sensitive comparison. This sorting strategy does not add any separators.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @author puce
 */
public class TextMenuItemSortingStrategy<MenuItem> implements MenuItemSortingStrategy<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>> {

    private final Collator collator;
    private final Function<MenuItem, String> menuTextExtractor;

    /**
     * Creates a new instance of the class.
     *
     * @param menuTextExtractor the GUI-toolkit specific function which provides the text of a menu item
     */
    public TextMenuItemSortingStrategy(Function<MenuItem, String> menuTextExtractor) {
        this(menuTextExtractor, Collator.getInstance(Locale.getDefault(Locale.Category.DISPLAY)));
    }

    /**
     * Creates a new instance of the class.
     *
     * @param menuTextExtractor the GUI-toolkit specific function which provides the text of a menu item
     * @param collator the {@link Collator} to use for the locale-sensitive text comparison
     */
    public TextMenuItemSortingStrategy(Function<MenuItem, String> menuTextExtractor, Collator collator) {
        this.menuTextExtractor = menuTextExtractor;
        this.collator = collator;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getMenuItemInsertionPoint(List<? extends MenuItemSupplierFactoryEntry<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>>> entryList,
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

    /**
     * This method will always return an empty {@link Optional}.
     *
     * {@inheritDoc }
     */
    @Override
    public Optional<Integer> getSeparatorInsertionPoint(int index,
            List<? extends MenuItemSupplierFactoryEntry<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>>> entryList,
            MenuItemSupplierFactoryEntry<MenuItem, IdentityMenuItemSupplierFactory<MenuItem>> entry) {
        return Optional.empty();
    }

}
