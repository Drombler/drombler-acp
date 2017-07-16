package org.drombler.acp.core.standard.status.impl;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import org.drombler.acp.core.status.StatusBarElement;
import org.drombler.commons.client.geometry.HorizontalAlignment;
import org.drombler.commons.fx.fxml.FXMLLoaders;
import org.drombler.commons.fx.scene.control.ProgressMonitor;

/**
 *
 * @author puce
 */

@StatusBarElement(horizontalAlignment = HorizontalAlignment.RIGHT, position = 20)
public class ProgressMonitorStatusBarElement extends BorderPane {

    @FXML
    private ProgressMonitor progressMonitor;

    public ProgressMonitorStatusBarElement() {
        FXMLLoaders.loadRoot(this);
    }

}
