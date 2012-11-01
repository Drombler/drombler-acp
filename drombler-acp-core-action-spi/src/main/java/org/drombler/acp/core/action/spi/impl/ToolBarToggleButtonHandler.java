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
package org.drombler.acp.core.action.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.drombler.acp.core.action.jaxb.ToolBarToggleEntryType;
import org.drombler.acp.core.action.jaxb.ToolBarsType;
import org.drombler.acp.core.action.spi.ToggleActionFactory;
import org.drombler.acp.core.action.spi.ToolBarToggleButtonFactory;
import org.drombler.acp.core.action.spi.ToolBarToggleEntryDescriptor;

/**
 *
 * @author puce
 */
@Component(immediate = true)
public class ToolBarToggleButtonHandler<ToolBar, ToolBarButton, ToolBarToggleButton extends ToolBarButton, ToggleAction>
        extends AbstractToolBarButtonHandler<ToolBar, ToolBarToggleButton, ToggleAction, ToolBarToggleEntryDescriptor> {

    @Reference
    private ToolBarToggleButtonFactory<ToolBarToggleButton, ToggleAction> toolBarToggleButtonFactory;
    @Reference
    private ToggleActionFactory<ToggleAction> actionFactory;

    protected void bindToolBarToggleButtonFactory(ToolBarToggleButtonFactory<ToolBarToggleButton, ToggleAction> toolBarToggleButtonFactory) {
        this.toolBarToggleButtonFactory = toolBarToggleButtonFactory;
        resolveUnresolvedItems();
    }

    protected void unbindToolBarToggleButtonFactory(ToolBarToggleButtonFactory<ToolBarToggleButton, ToggleAction> toolBarToggleButtonFactory) {
        this.toolBarToggleButtonFactory = null;
    }

    protected void bindToggleActionFactory(ToggleActionFactory<ToggleAction> actionFactory) {
        this.actionFactory = actionFactory;
        resolveUnresolvedItems();
    }

    protected void unbindToggleActionFactory(ToggleActionFactory<ToggleAction> actionFactory) {
        this.actionFactory = null;
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && toolBarToggleButtonFactory != null && actionFactory != null;
    }

    @Override
    protected Class<ToggleAction> getActionClass() {
        return actionFactory.getToggleActionClass();
    }

    @Override
    protected ToolBarToggleButton createToolBarButton(ToolBarToggleEntryDescriptor toolBarEntryDescriptor, ToggleAction action, int ICON_SIZE) {
        return toolBarToggleButtonFactory.createToolBarToggleButton(toolBarEntryDescriptor, action, ICON_SIZE);
    }

    @Override
    protected void resolveToolBarsType(ToolBarsType toolBarsType, Bundle bundle, BundleContext context) {
        for (ToolBarToggleEntryType toolBarEntry : toolBarsType.getToolBarToggleEntry()) {
            ToolBarToggleEntryDescriptor toolBarEntryDescriptor = ToolBarToggleEntryDescriptor.createToolBarToggleEntryDescriptor(
                    toolBarEntry);
            resolveToolBarEntry(toolBarEntryDescriptor, context);
        }
    }
}