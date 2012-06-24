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
package org.richclientplatform.core.standard.docking;

import org.richclientplatform.core.docking.DockingArea;
import org.richclientplatform.core.docking.DockingAreaKind;
import org.richclientplatform.core.docking.DockingAreas;
import org.richclientplatform.core.docking.LayoutConstraints;
