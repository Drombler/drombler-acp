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
@Action(id = "standard.save", category = "core", displayName = "#save.displayName", accelerator = "Shortcut+S", icon = "save.gif")
@MenuEntry(path = "File", position = 4200)
@ToolBarEntry(toolBarId = "file", position = 50)
public class SaveAction extends AbstractActionListener<Object> {

    @Override
    public void actionPerformed(Object event) {
        System.out.println("save");
    }
}
