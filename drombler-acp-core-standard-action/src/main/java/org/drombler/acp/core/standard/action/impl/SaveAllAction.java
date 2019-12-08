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
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.action.ToolBarEntry;
import org.drombler.commons.action.command.Savable;
import org.drombler.commons.action.context.AbstractApplicationContextSensitiveActionListener;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.drombler.commons.context.ContextEvent;

/**
 *
 * @author puce
 */
@Action(id = "standard.saveAll", category = "core", displayName = "%saveAll.displayName", accelerator = "Shortcut+Shift+S", icon = "saveAll.png",
        resourceBundleBaseName = ResourceBundleUtils.PACKAGE_RESOURCE_BUNDLE_BASE_NAME)
@MenuEntry(path = "File", position = 4210)
@ToolBarEntry(toolBarId = "file", position = 60)
public class SaveAllAction extends AbstractApplicationContextSensitiveActionListener<Savable, Object> {

    private Collection<? extends Savable> savables = Collections.emptyList();

    public SaveAllAction() {
        super(Savable.class);
        setEnabled(false);
    }

    @Override
    public void onAction(Object event) {
        List<Savable> currentSavables = new ArrayList<>(savables); // protect against modification during iteration TODO: needed?
        currentSavables.forEach(Savable::save);
    }

    @Override
    protected void contextChanged(ContextEvent<Savable> event) {
        savables = getApplicationContext().findAll(event.getType());
        setEnabled(!savables.isEmpty());
    }
}
