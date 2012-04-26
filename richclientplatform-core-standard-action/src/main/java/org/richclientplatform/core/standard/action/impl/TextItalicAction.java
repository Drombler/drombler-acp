/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.standard.action.impl;

import org.richclientplatform.core.action.AbstractToggleActionListener;
import org.richclientplatform.core.action.ToggleAction;
import org.richclientplatform.core.action.ToggleMenuEntry;
import org.richclientplatform.core.action.ToolBarToggleEntry;

/**
 *
 * @author puce
 */
@ToggleAction(id = "standard.text.italic", category = "core", displayName = "#textItalic.displayName", accelerator = "Shortcut+I", icon = "undo.png")
@ToggleMenuEntry(path = "Edit", position = 3210)
@ToolBarToggleEntry(toolBarId = "edit", position = 260)
public class TextItalicAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Italic: " + newValue);
    }
}
