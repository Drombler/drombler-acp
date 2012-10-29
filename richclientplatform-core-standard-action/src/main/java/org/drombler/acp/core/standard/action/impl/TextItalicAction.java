/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.standard.action.impl;

import org.drombler.acp.core.action.AbstractToggleActionListener;
import org.drombler.acp.core.action.ToggleAction;
import org.drombler.acp.core.action.ToggleMenuEntry;
import org.drombler.acp.core.action.ToolBarToggleEntry;

/**
 *
 * @author puce
 */
@ToggleAction(id = "standard.text.italic", category = "core", displayName = "#textItalic.displayName", accelerator = "Shortcut+I", icon = "undo.png")
//@ToggleMenuEntry(path = "Edit", position = 3210)
//@ToolBarToggleEntry(toolBarId = "edit", position = 260)
public class TextItalicAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Italic: " + newValue);
    }
}
