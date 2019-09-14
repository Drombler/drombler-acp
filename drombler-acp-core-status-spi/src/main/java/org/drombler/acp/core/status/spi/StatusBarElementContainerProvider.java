package org.drombler.acp.core.status.spi;

/**
 * A {@link StatusBarElementContainer} provider.<br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @author puce
 * @param <T> the GUI-toolkit specific base type of status bar elements
 */
public interface StatusBarElementContainerProvider<T> {

    /**
     * Gets a {@link StatusBarElementContainer}.
     *
     * @return a status bar element container
     */
    StatusBarElementContainer<T> getStatusBarElementContainer();
}
