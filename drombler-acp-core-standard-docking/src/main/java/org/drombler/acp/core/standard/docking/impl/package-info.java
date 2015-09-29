/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2012 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
@DockingArea(id = "center", kind = DockingAreaKind.EDITOR, position = 20,
        path = {20, 40, 50}, permanent = true)
@DockingArea(id = "top", kind = DockingAreaKind.VIEW, position = 20,
        path = {20, 40, 20}, layoutConstraints = @LayoutConstraints(prefHeight = 100))
@DockingArea(id = "bottom", kind = DockingAreaKind.VIEW, position = 20,
        path = {20, 40, 80}, layoutConstraints = @LayoutConstraints(prefHeight = 100))
@DockingArea(id = "left", kind = DockingAreaKind.VIEW, position = 20,
        path = {20, 20}, layoutConstraints = @LayoutConstraints(prefWidth = 200))
@DockingArea(id = "right", kind = DockingAreaKind.VIEW, position = 20,
        path = {20, 80}, layoutConstraints = @LayoutConstraints(prefWidth = 200))
package org.drombler.acp.core.standard.docking.impl;

import org.drombler.acp.core.docking.DockingArea;
import org.drombler.acp.core.docking.LayoutConstraints;
import org.drombler.commons.docking.DockingAreaKind;
