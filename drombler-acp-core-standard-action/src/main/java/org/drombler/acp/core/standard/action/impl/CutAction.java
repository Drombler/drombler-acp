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
@Action(id = "standard.cut", category = "core", displayName = "#cut.displayName", accelerator = "Shortcut+X", icon = "cut.png")
//@MenuEntry(path = "Edit", position = 2100)
//@ToolBarEntry(toolBarId = "edit", position = 50)
public class CutAction extends AbstractActionListener<Object> {

    @Override
    public void onAction(Object event) {
        System.out.println("cut");
    }
}
