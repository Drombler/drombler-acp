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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.drombler.acp.core.action.AbstractActionListener;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.action.ToolBarEntry;
import org.drombler.commons.context.ApplicationContextSensitive;
import org.drombler.commons.context.Context;
import org.drombler.commons.context.ContextEvent;
import org.drombler.commons.context.ContextListener;
import org.drombler.acp.core.standard.action.Savable;

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
/**
 *
 * @author puce
 */
@Action(id = "standard.saveAll", category = "core", displayName = "%saveAll.displayName", accelerator = "Shortcut+Shift+S", icon = "saveAll.png")
@MenuEntry(path = "File", position = 4210)
@ToolBarEntry(toolBarId = "file", position = 60)
public class SaveAllAction extends AbstractActionListener<Object> implements ApplicationContextSensitive {

    private Collection<? extends Savable> savables = Collections.emptyList();
    private Context applicationContext;

    public SaveAllAction() {
        setDisabled(true);
    }

    @Override
    public void onAction(Object event) {
        List<Savable> currentSavables = new ArrayList<>(savables); // protect against modification during iteration TODO: needed?
        for (Savable savable : currentSavables) {
            savable.save();
        }
    }

    @Override
    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
        this.applicationContext.addContextListener(Savable.class, new ContextListener() {

            @Override
            public void contextChanged(ContextEvent event) {
                SaveAllAction.this.contextChanged();
            }
        });
        contextChanged();
    }

    private void contextChanged() {
        savables = applicationContext.findAll(Savable.class);
        setDisabled(savables.isEmpty());
    }
}
