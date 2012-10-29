/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.drombler.acp.core.action.jaxb.ToolBarEntryType;
import org.drombler.acp.core.action.jaxb.ToolBarsType;
import org.drombler.acp.core.action.spi.ActionFactory;
import org.drombler.acp.core.action.spi.ToolBarButtonFactory;
import org.drombler.acp.core.action.spi.ToolBarEntryDescriptor;

/**
 *
 * @author puce
 */
@Component(immediate = true)
public class ToolBarButtonHandler<ToolBar, ToolBarButton, Action>
        extends AbstractToolBarButtonHandler<ToolBar, ToolBarButton, Action, ToolBarEntryDescriptor> {

    @Reference
    private ToolBarButtonFactory<ToolBarButton, Action> toolBarButtonFactory;
    @Reference
    private ActionFactory<Action> actionFactory;

    protected void bindToolBarButtonFactory(ToolBarButtonFactory<ToolBarButton, Action> toolBarButtonFactory) {
        this.toolBarButtonFactory = toolBarButtonFactory;
        resolveUnresolvedItems();
    }

    protected void unbindToolBarButtonFactory(ToolBarButtonFactory<ToolBarButton, Action> toolBarButtonFactory) {
        this.toolBarButtonFactory = null;
    }

    protected void bindActionFactory(ActionFactory<Action> actionFactory) {
        this.actionFactory = actionFactory;
        resolveUnresolvedItems();
    }

    protected void unbindActionFactory(ActionFactory<Action> actionFactory) {
        this.actionFactory = null;
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && toolBarButtonFactory != null && actionFactory != null;
    }

    @Override
    protected Class<Action> getActionClass() {
        return actionFactory.getActionClass();
    }

    @Override
    protected ToolBarButton createToolBarButton(ToolBarEntryDescriptor toolBarEntryDescriptor, Action action, int iconSize) {
        return toolBarButtonFactory.createToolBarButton(toolBarEntryDescriptor, action, iconSize);
    }

    @Override
    protected void resolveToolBarsType(ToolBarsType toolBarsType, Bundle bundle, BundleContext context) {
        for (ToolBarEntryType toolBarEntry : toolBarsType.getToolBarEntry()) {
            ToolBarEntryDescriptor toolBarEntryDescriptor = ToolBarEntryDescriptor.createToolBarEntryDescriptor(toolBarEntry);
            resolveToolBarEntry(toolBarEntryDescriptor, context);
        }
    }
}
