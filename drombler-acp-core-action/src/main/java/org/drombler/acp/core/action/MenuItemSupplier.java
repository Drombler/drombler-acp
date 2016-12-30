package org.drombler.acp.core.action;

/**
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @author puce
 */
public interface MenuItemSupplier<MenuItem> {
    <T extends MenuItem> T getMenuItem(); // TODO remove T?
}
