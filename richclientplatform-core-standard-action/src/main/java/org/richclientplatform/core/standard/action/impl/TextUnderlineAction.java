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
@ToggleAction(id = "standard.text.underline", category = "core", displayName = "#textUnderline.displayName", accelerator = "Shortcut+U", icon = "paste.png")
@ToggleMenuEntry(path = "Edit", position = 3220)
@ToolBarToggleEntry(toolBarId = "edit", position = 270)
public class TextUnderlineAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Underline: " + newValue);
    }
}
