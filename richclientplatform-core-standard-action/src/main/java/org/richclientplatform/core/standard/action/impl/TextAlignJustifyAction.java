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
@ToggleAction(id = "standard.text.alignJustify", category = "core", displayName = "#textAlignJustify.displayName", accelerator = "Shortcut+J", icon = "copy.png")
@ToggleMenuEntry(path = "Edit", position = 1210, toggleGroupId="text.align")
@ToolBarToggleEntry(toolBarId = "edit", position = 160, toggleGroupId="text.align")
public class TextAlignJustifyAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Align Justify: " + newValue);
    }
}
