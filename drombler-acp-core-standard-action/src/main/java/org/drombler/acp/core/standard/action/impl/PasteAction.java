package org.drombler.acp.core.standard.action.impl;

import org.drombler.acp.core.action.AbstractActionListener;
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
@Action(id = "standard.paste", category = "core", displayName = "#paste.displayName", accelerator = "Shortcut+V", icon = "paste.png")
//@MenuEntry(path = "Edit", position = 2120)
//@ToolBarEntry(toolBarId = "edit", position = 70)
public class PasteAction extends AbstractActionListener<Object> {

    @Override
    public void onAction(Object event) {
        System.out.println("paste");
    }
}
