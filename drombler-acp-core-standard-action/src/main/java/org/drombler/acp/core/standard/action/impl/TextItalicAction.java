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
//@ToggleAction(id = "standard.text.italic", category = "core", displayName = "%textItalic.displayName", accelerator = "Shortcut+I", icon = "undo.png",
//        resourceBundleBaseName = ResourceBundleUtils.PACKAGE_RESOURCE_BUNDLE_BASE_NAME)
//@ToggleMenuEntry(path = "Edit", position = 3210)
//@ToolBarToggleEntry(toolBarId = "edit", position = 260)
public class TextItalicAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Italic: " + newValue);
    }
}
