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
@Action(id = "standard.copy", category = "core", displayName = "#copy.displayName", accelerator = "Shortcut+C", icon = "copy.gif")
@MenuEntry(path = "Edit", position = 2110)
@ToolBarEntry(toolBarId = "edit", position = 60)
public class CopyAction extends AbstractActionListener<Object> {

    @Override
    public void onAction(Object event) {
        System.out.println("copy");
    }
}
