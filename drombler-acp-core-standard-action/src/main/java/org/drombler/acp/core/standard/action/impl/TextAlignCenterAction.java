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
package org.drombler.acp.core.standard.action.impl;

import org.drombler.commons.action.AbstractToggleActionListener;
import org.drombler.acp.core.action.ToggleAction;
import org.drombler.acp.core.action.ToggleMenuEntry;
import org.drombler.acp.core.action.ToolBarToggleEntry;

/**
 *
 * @author puce
 */
@ToggleAction(id = "standard.text.alignCenter", category = "core", displayName = "%textAlignCenter.displayName", accelerator = "Shortcut+E", icon = "cut.png")
//@ToggleMenuEntry(path = "Edit/Align", position = 1200, toggleGroupId="text.align")
//@ToolBarToggleEntry(toolBarId = "align", position = 150, toggleGroupId="text.align")
public class TextAlignCenterAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Align Center: " + newValue);
    }
}
