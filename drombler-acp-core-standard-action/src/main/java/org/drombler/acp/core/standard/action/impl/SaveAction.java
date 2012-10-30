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

import org.drombler.acp.core.action.AbstractActionListener;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.action.ToolBarEntry;
import org.drombler.acp.core.lib.util.context.ActiveContextSensitive;
import org.drombler.acp.core.lib.util.context.Context;
import org.drombler.acp.core.lib.util.context.ContextEvent;
import org.drombler.acp.core.lib.util.context.ContextListener;
import org.drombler.acp.core.standard.action.Savable;

/**
 *
 * @author puce
 */
@Action(id = "standard.save", category = "core", displayName = "#save.displayName", accelerator = "Shortcut+S", icon = "save.gif")
@MenuEntry(path = "File", position = 4200)
@ToolBarEntry(toolBarId = "file", position = 50)
public class SaveAction extends AbstractActionListener<Object> implements ActiveContextSensitive {

    private Savable savable;
    private Context activeContext;

    public SaveAction() {
        setDisabled(true);
    }

    @Override
    public void onAction(Object event) {
        savable.save();
    }

    @Override
    public void setActiveContext(Context activeContext) {
        this.activeContext = activeContext;
        this.activeContext.addContextListener(Savable.class, new ContextListener() {

            @Override
            public void contextChanged(ContextEvent event) {
                SaveAction.this.contextChanged();
            }
        });
        contextChanged();
    }

    private void contextChanged() {
        savable = activeContext.find(Savable.class);
        setDisabled(savable == null);
    }
}
