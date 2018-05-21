package org.drombler.acp.core.status.spi;

/**
 *
 * @author puce
 * @param <T>
 */


public interface StatusBarElementContainerProvider<T> {

    StatusBarElementContainer<T> getStatusBarElementContainer();
}
