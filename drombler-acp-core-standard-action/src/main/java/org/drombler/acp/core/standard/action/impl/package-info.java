/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2014 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
@Menu(id = "File", displayName = "%fileMenu.displayName", position = 10) //    @Menu(id = "Edit", displayName = "%editMenu.displayName", position = 20)
        //    @Menu(id = "Align", displayName = "Align", path = "Edit", position = 10)
@Menu(id = "View", displayName = "%viewMenu.displayName", position = 40)
@Menu(id = "Toolbars", displayName = "%toolbarsMenu.displayName", path = "View", position = 5100)
@Menu(id = "Help", displayName = "%helpMenu.displayName", position = 900)
@ToolBar(id = "file", displayName = "%fileToolBar.displayName", position = 10)
//    @ToolBar(id = "edit", displayName = "%editToolBar.displayName", position = 30)
//    @ToolBar(id = "align", displayName = "Align", position = 40)
package org.drombler.acp.core.standard.action.impl;

import org.drombler.acp.core.action.Menu;
import org.drombler.acp.core.action.ToolBar;
