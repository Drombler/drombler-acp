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
@Action(id = "standard.paste", category = "core", displayName = "#paste.displayName", accelerator = "Shortcut+V", icon = "paste.png")
@MenuEntry(path = "Edit", position = 2120)
@ToolBarEntry(toolBarId = "edit", position = 70)
public class PasteAction extends AbstractActionListener<Object> {

    @Override
    public void onAction(Object event) {
        System.out.println("paste");
    }
}
