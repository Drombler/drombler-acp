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
@Action(id = "standard.delete", category = "core", displayName = "#delete.displayName", accelerator = "Delete", icon = "delete.png")
//@MenuEntry(path = "Edit", position = 2130)
//@ToolBarEntry(toolBarId = "edit", position = 80)
public class DeleteAction extends AbstractActionListener<Object> {

    @Override
    public void onAction(Object event) {
        System.out.println("delete");
    }
}
