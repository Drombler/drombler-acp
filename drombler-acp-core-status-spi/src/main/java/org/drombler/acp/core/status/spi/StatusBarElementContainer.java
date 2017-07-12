package org.drombler.acp.core.status.spi;

import org.softsmithy.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
public interface StatusBarElementContainer<T> {

    void addLeftStatusBarElement(PositionableAdapter<? extends T> statusBarElement);

    void addCenterStatusBarElement(PositionableAdapter<? extends T> statusBarElement);

    void addRightStatusBarElement(PositionableAdapter<? extends T> statusBarElement);

    boolean isLeftToRight();

    boolean isMirroringEnabled();

}
