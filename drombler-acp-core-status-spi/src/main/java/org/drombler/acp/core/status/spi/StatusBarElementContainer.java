package org.drombler.acp.core.status.spi;

import javafx.scene.Node;
import org.softsmithy.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */


public interface StatusBarElementContainer {

    void addLeftStatusBarElement(PositionableAdapter<? extends Node> statusBarElement);

    void addCenterStatusBarElement(PositionableAdapter<? extends Node> statusBarElement);

    void addRightStatusBarElement(PositionableAdapter<? extends Node> statusBarElement);

}
