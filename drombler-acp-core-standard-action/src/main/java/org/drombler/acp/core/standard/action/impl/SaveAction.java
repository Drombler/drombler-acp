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

import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.action.ToolBarEntry;
import org.drombler.commons.action.command.Savable;
import org.drombler.commons.action.context.AbstractActiveContextSensitiveActionListener;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.drombler.commons.context.ContextEvent;

/**
 *
 * @author puce
 */
@Action(id = "standard.save", category = "core", displayName = "%save.displayName",
        accelerator = "Shortcut+S", icon = "save.gif",
        resourceBundleBaseName = ResourceBundleUtils.PACKAGE_RESOURCE_BUNDLE_BASE_NAME)
@MenuEntry(path = "File", position = 4200)
@ToolBarEntry(toolBarId = "file", position = 50)
public class SaveAction extends AbstractActiveContextSensitiveActionListener<Savable, Object> {

    private Savable savable;

    public SaveAction() {
        super(Savable.class);
        setEnabled(false);
    }

    @Override
    public void onAction(Object event) {
        savable.save();
    }

    @Override
    protected void contextChanged(ContextEvent<Savable> contextEvent) {
        savable = getActiveContext().find(contextEvent.getType());
        setEnabled(savable != null);
    }
}
