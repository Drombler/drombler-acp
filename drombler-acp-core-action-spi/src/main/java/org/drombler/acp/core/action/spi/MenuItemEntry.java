package org.drombler.acp.core.action.spi;

/**
 *
 * @author puce
 */


public class MenuItemEntry<MenuItem> {

    private final int index;
    private final MenuItem menuItem;

    public MenuItemEntry(int index, MenuItem menuItemSupplier) {
        this.index = index;
        this.menuItem = menuItemSupplier;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the menuItem
     */
    public MenuItem getMenuItem() {
        return menuItem;
    }

}
