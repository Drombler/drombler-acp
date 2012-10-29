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
@ToggleAction(id = "standard.text.alignLeft", category = "core", displayName = "#textAlignLeft.displayName", accelerator = "Shortcut+L", icon = "paste.png")
//@ToggleMenuEntry(path = "Edit/Align", position = 1220, toggleGroupId="text.align")
//@ToolBarToggleEntry(toolBarId = "align", position = 170, toggleGroupId="text.align")
public class TextAlignLeftAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Align Left: " + newValue);
    }
}
