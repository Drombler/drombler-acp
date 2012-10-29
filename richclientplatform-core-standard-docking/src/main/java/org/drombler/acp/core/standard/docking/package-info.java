/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
@DockingAreas({
    @DockingArea(id = "center", kind = DockingAreaKind.EDITOR, position = 20, path = {20, 40, 50}, permanent = true),
    @DockingArea(id = "top", kind = DockingAreaKind.VIEW, position = 20, path = {20, 40, 20},
    layoutConstraints = @LayoutConstraints(prefHeight = 100)),
    @DockingArea(id = "bottom", kind = DockingAreaKind.VIEW, position = 20, path = {20, 40, 80},
    layoutConstraints = @LayoutConstraints(prefHeight = 100)),
    @DockingArea(id = "left", kind = DockingAreaKind.VIEW, position = 20, path = {20, 20},
    layoutConstraints = @LayoutConstraints(prefWidth = 200)),
    @DockingArea(id = "right", kind = DockingAreaKind.VIEW, position = 20, path = {20, 80},
    layoutConstraints = @LayoutConstraints(prefWidth = 200))
})
package org.drombler.acp.core.standard.docking;

import org.drombler.acp.core.docking.DockingArea;
import org.drombler.acp.core.docking.DockingAreaKind;
import org.drombler.acp.core.docking.DockingAreas;
import org.drombler.acp.core.docking.LayoutConstraints;
