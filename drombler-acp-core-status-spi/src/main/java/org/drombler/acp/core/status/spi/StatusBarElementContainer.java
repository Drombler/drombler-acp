package org.drombler.acp.core.status.spi;

import org.softsmithy.lib.util.PositionableAdapter;

/**
 * A status bar element container.
 *
 * @author puce
 * @param <T> the GUI-toolkit specific base type of status bar elements
 */
public interface StatusBarElementContainer<T> {

    /**
     * Adds a status bar element to the left side of the container.
     *
     * @param statusBarElement the status bar element to add
     */
    void addLeftStatusBarElement(PositionableAdapter<? extends T> statusBarElement);

    /**
     * Adds a status bar element to the center of the container.
     *
     * @param statusBarElement the status bar element to add
     */
    void addCenterStatusBarElement(PositionableAdapter<? extends T> statusBarElement);

    /**
     * Adds a status bar element to the right side of the container.
     *
     * @param statusBarElement the status bar element to add
     */
    void addRightStatusBarElement(PositionableAdapter<? extends T> statusBarElement);

    /**
     * Indicates if the container is oriented left-to-right.
     *
     * @return true, if the container is oriented left-to-right, else false
     */
    boolean isLeftToRight();

    /**
     * Indicates if mirroring is enabled in case of right-to-left orientation.
     *
     * @return true, if mirroring is enabled in case of right-to-left orientation, else false
     */
    boolean isMirroringEnabled();

}
