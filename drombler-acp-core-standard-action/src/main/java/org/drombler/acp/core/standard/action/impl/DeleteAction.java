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
 * Copyright 2012 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.standard.action.impl;

import org.drombler.commons.action.AbstractActionListener;

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
/**
 *
 * @author puce
 */
//@Action(id = "standard.delete", category = "core", displayName = "%delete.displayName", accelerator = "Delete", icon = "delete.png",
//        resourceBundleBaseName = ResourceBundleUtils.PACKAGE_RESOURCE_BUNDLE_BASE_NAME)
//@MenuEntry(path = "Edit", position = 2130)
//@ToolBarEntry(toolBarId = "edit", position = 80)
public class DeleteAction extends AbstractActionListener<Object> {

    @Override
    public void onAction(Object event) {
        System.out.println("delete");
    }
}
