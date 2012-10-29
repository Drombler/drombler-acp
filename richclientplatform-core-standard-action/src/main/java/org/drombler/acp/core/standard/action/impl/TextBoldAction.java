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
@ToggleAction(id = "standard.text.bold", category = "core", displayName = "#textBold.displayName", accelerator = "Shortcut+B", icon = "cut.png")
//@ToggleMenuEntry(path = "Edit", position = 3200)
//@ToolBarToggleEntry(toolBarId = "edit", position = 250)
public class TextBoldAction extends AbstractToggleActionListener<Object> {

    @Override
    public void onSelectionChanged(boolean oldValue, boolean newValue) {
        System.out.println("Text Bold: " + newValue);
    }
}
