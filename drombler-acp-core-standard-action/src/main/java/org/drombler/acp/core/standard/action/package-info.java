/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
@Menus({
    @Menu(id = "File", displayName = "#fileMenu.displayName", position = 10),
//    @Menu(id = "Edit", displayName = "#editMenu.displayName", position = 20),
//    @Menu(id = "Align", displayName = "Align", path = "Edit", position = 10),
    @Menu(id = "View", displayName = "#viewMenu.displayName", position = 40),
    @Menu(id = "Toolbars", displayName = "#toolbarsMenu.displayName", path = "View", position = 5100),
    @Menu(id = "Help", displayName = "#helpMenu.displayName", position = 900)})
@ToolBars({
    @ToolBar(id = "file", displayName = "#fileToolBar.displayName", position = 10)//,
//    @ToolBar(id = "edit", displayName = "#editToolBar.displayName", position = 30),
//    @ToolBar(id = "align", displayName = "Align", position = 40)
})
package org.drombler.acp.core.standard.action;

import org.drombler.acp.core.action.Menu;
import org.drombler.acp.core.action.Menus;
import org.drombler.acp.core.action.ToolBar;
import org.drombler.acp.core.action.ToolBars;
