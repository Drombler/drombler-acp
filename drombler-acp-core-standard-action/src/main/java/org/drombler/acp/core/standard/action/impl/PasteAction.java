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

import org.drombler.commons.action.AbstractActionListener;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.action.ToolBarEntry;

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
/**
 *
 * @author puce
 */
@Action(id = "standard.paste", category = "core", displayName = "%paste.displayName", accelerator = "Shortcut+V", icon = "paste.png")
//@MenuEntry(path = "Edit", position = 2120)
//@ToolBarEntry(toolBarId = "edit", position = 70)
public class PasteAction extends AbstractActionListener<Object> {

    @Override
    public void onAction(Object event) {
        System.out.println("paste");
    }
}
