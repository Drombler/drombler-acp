/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
@Menus({
    @Menu(id = "File", displayName = "#fileMenu.displayName", position = 10),
    @Menu(id = "Edit", displayName = "#editMenu.displayName", position = 20),
    @Menu(id = "Help", displayName = "#helpMenu.displayName", position = 900)})
@ToolBars({
    @ToolBar(id = "file", displayName = "#fileToolBar.displayName", position = 10)
})
package org.richclientplatform.core.standard.action;

import org.richclientplatform.core.action.Menu;
import org.richclientplatform.core.action.Menus;
import org.richclientplatform.core.action.ToolBar;
import org.richclientplatform.core.action.ToolBars;
