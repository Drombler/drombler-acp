package org.drombler.acp.core.status.spi;

/**
 *
 * @author puce
 */


public interface StatusBarElementContainerProvider<T> {

    StatusBarElementContainer<T> getStatusBarElementContainer();
}
