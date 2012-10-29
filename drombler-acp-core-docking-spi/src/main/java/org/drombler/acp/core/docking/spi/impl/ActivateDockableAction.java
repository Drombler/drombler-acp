/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking.spi.impl;

import org.drombler.acp.core.action.AbstractActionListener;
import org.drombler.acp.core.docking.Dockable;

/**
 *
 * @author puce
 */
public class ActivateDockableAction extends AbstractActionListener<Object> {

    private final Dockable dockable;

    public ActivateDockableAction(Dockable dockable) {
        this.dockable = dockable;
    }

    @Override
    public void onAction(Object event) {
        dockable.open();
        dockable.requestActive();
    }
}
