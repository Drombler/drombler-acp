/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.richclientplatform.core.action.jaxb.ToolBarToggleEntryType;
import org.richclientplatform.core.action.jaxb.ToolBarsType;
import org.richclientplatform.core.action.spi.ToggleActionFactory;
import org.richclientplatform.core.action.spi.ToolBarToggleButtonFactory;
import org.richclientplatform.core.action.spi.ToolBarToggleEntryDescriptor;

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
        return toolBarToggleButtonFactory.createToolToggleBarButton(toolBarEntryDescriptor, action, ICON_SIZE);
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
