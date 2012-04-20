package org.richclientplatform.core.standard.action.impl;

import org.richclientplatform.core.action.AbstractActionListener;
import org.richclientplatform.core.action.Action;
import org.richclientplatform.core.action.MenuEntry;
import org.richclientplatform.core.action.ToolBarEntry;

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
/**
 *
 * @author puce
 */
@Action(id = "standard.delete", category = "core", displayName = "#delete.displayName", accelerator = "Del", icon = "delete.gif")
@MenuEntry(path = "Edit", position = 2130)
@ToolBarEntry(toolBarId = "edit", position = 80)
public class DeleteAction extends AbstractActionListener<Object> {

    @Override
    public void onAction(Object event) {
        System.out.println("delete");
    }
}
