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
@ToggleAction(id = "standard.text.alignRight", category = "core", displayName = "#textAlignRight.displayName", accelerator = "Shortcut+R", icon = "delete.png")
@ToggleMenuEntry(path = "Edit", position = 1230, toggleGroupId="text.align")
@ToolBarToggleEntry(toolBarId = "edit", position = 180, toggleGroupId="text.align")
public class TextAlignRightAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Align Right: " + newValue);
    }
}
