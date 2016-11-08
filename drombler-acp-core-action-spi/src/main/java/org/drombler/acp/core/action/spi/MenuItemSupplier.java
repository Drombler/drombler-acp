package org.drombler.acp.core.action.spi;

/**
 *
 * @author puce
 */


public interface MenuItemSupplier<MenuItem> {
    <T extends MenuItem> T getMenuItem(); // TODO remove T?
}
