package org.richclientplatform.core.standard.action.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.richclientplatform.core.action.AbstractActionListener;
import org.richclientplatform.core.action.Action;
import org.richclientplatform.core.action.MenuEntry;
import org.richclientplatform.core.action.ToolBarEntry;
import org.richclientplatform.core.standard.action.Savable;

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
/**
 *
 * @author puce
 */
@Action(id = "standard.saveAll", category = "core", displayName = "#saveAll.displayName", accelerator = "Shortcut+Shift+S", icon = "saveAll.png")
@MenuEntry(path = "File", position = 4210)
@ToolBarEntry(toolBarId = "file", position = 60)
public class SaveAllAction extends AbstractActionListener<Object> {

    private final List<Savable> savables = new LinkedList<>(); // TODO: LinkedList?

    @Override
    public void onAction(Object event) {
        System.out.println("save all");
        List<Savable> currentSavables = new ArrayList<>(savables); // protect against modification during iteration TODO: needed?
        for (Savable savable : currentSavables) {
            savable.save();
        }
    }
}
