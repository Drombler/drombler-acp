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

/**
 *
 * @author puce
 */
//@ToggleAction(id = "standard.text.alignJustify", category = "core", displayName = "%textAlignJustify.displayName", accelerator = "Shortcut+J", icon = "copy.png",
//        resourceBundleBaseName = ResourceBundleUtils.PACKAGE_RESOURCE_BUNDLE_BASE_NAME)
//@ToggleMenuEntry(path = "Edit/Align", position = 1210, toggleGroupId="text.align")
//@ToolBarToggleEntry(toolBarId = "align", position = 160, toggleGroupId="text.align")
public class TextAlignJustifyAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Align Justify: " + newValue);
    }
}
