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
@ToggleAction(id = "standard.text.alignJustify", category = "core", displayName = "#textAlignJustify.displayName", accelerator = "Shortcut+J", icon = "copy.png")
//@ToggleMenuEntry(path = "Edit/Align", position = 1210, toggleGroupId="text.align")
//@ToolBarToggleEntry(toolBarId = "align", position = 160, toggleGroupId="text.align")
public class TextAlignJustifyAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Align Justify: " + newValue);
    }
}
