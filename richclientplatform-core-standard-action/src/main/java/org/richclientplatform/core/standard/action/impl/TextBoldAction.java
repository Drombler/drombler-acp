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
@ToggleAction(id = "standard.text.bold", category = "core", displayName = "#textBold.displayName", accelerator = "Shortcut+B", icon = "cut.png")
@ToggleMenuEntry(path = "Edit", position = 3200)
@ToolBarToggleEntry(toolBarId = "edit", position = 250)
public class TextBoldAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Bold: " + newValue);
    }
}
