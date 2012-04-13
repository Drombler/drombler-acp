/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
 @DockingAreas({
    @DockingArea(id = "left", kind = DockingAreaKind.VIEW, 
            path = {
                @DockingAreaPath(orientation=Orientation.VERTICAL, position=20),
                @DockingAreaPath(orientation=Orientation.HORIZONTAL, position=20),
                @DockingAreaPath(orientation=Orientation.VERTICAL, position=20)
            }),
    @DockingArea(id = "right", kind = DockingAreaKind.VIEW, 
            path = {
                @DockingAreaPath(orientation=Orientation.VERTICAL, position=20),
                @DockingAreaPath(orientation=Orientation.HORIZONTAL, position=80),
                @DockingAreaPath(orientation=Orientation.VERTICAL, position=20)
            }),
    @DockingArea(id = "top", kind = DockingAreaKind.VIEW, 
            path = {
                @DockingAreaPath(orientation=Orientation.VERTICAL, position=20),
                @DockingAreaPath(orientation=Orientation.HORIZONTAL, position=40),
                @DockingAreaPath(orientation=Orientation.VERTICAL, position=20),
                @DockingAreaPath(orientation=Orientation.HORIZONTAL, position=20)
            }),
    @DockingArea(id = "bottom", kind = DockingAreaKind.VIEW, 
            path = {
                @DockingAreaPath(orientation=Orientation.VERTICAL, position=20),
                @DockingAreaPath(orientation=Orientation.HORIZONTAL, position=40),
                @DockingAreaPath(orientation=Orientation.VERTICAL, position=80),
                @DockingAreaPath(orientation=Orientation.HORIZONTAL, position=20)
            })
})
package org.richclientplatform.core.standard.docking;

import org.richclientplatform.core.docking.DockingArea;
import org.richclientplatform.core.docking.DockingAreaKind;
import org.richclientplatform.core.docking.DockingAreaPath;
import org.richclientplatform.core.docking.DockingAreas;
import org.richclientplatform.core.lib.geometry.Orientation;
