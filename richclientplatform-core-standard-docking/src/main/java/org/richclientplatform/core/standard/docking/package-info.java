/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
@DockingAreas({
    @DockingArea(id = "left", kind = DockingAreaKind.VIEW, position = 20, path = {20, 20}),
    @DockingArea(id = "right", kind = DockingAreaKind.VIEW, position = 20, path = {20, 80}),
    @DockingArea(id = "top", kind = DockingAreaKind.VIEW, position = 20, path = {20, 40, 20}),
    @DockingArea(id = "bottom", kind = DockingAreaKind.VIEW, position = 20, path = {20, 40, 80})
})
package org.richclientplatform.core.standard.docking;

import org.richclientplatform.core.docking.DockingArea;
import org.richclientplatform.core.docking.DockingAreaKind;
import org.richclientplatform.core.docking.DockingAreas;
