/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing.impl;

import org.richclientplatform.core.action.AbstractActionListener;
import org.richclientplatform.core.docking.Dockable;

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
    public void actionPerformed(Object event) {
        dockable.open();
        dockable.requestActive();
    }
}
