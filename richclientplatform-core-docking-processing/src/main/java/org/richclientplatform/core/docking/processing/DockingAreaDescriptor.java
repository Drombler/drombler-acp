/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing;

import org.richclientplatform.core.docking.jaxb.DockingAreaType;

/**
 *
 * @author puce
 */
public class DockingAreaDescriptor {

    public static DockingAreaDescriptor createDockingAreaDescriptor(DockingAreaType dockingArea) {
        DockingAreaDescriptor dockingAreaDescriptor = new DockingAreaDescriptor();
        return dockingAreaDescriptor;
    }
    
}
