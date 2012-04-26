/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.richclientplatform.core.action.jaxb.ToolBarEntryType;
import org.richclientplatform.core.action.jaxb.ToolBarsType;
import org.richclientplatform.core.action.spi.ActionFactory;
import org.richclientplatform.core.action.spi.ActionRegistry;
import org.richclientplatform.core.action.spi.ToolBarButtonFactory;
import org.richclientplatform.core.action.spi.ToolBarContainerListenerAdapter;
import org.richclientplatform.core.action.spi.ToolBarContainerToolBarEvent;
import org.richclientplatform.core.action.spi.ToolBarEntryDescriptor;
import org.richclientplatform.core.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
@Component(immediate = true)
public class ToolBarButtonHandler<ToolBar, ToolBarButton, Action> extends AbstractToolBarHandler<ToolBar, ToolBarButton> {

    private static final int ICON_SIZE = 24;
    @Reference
    private ToolBarButtonFactory<ToolBarButton, Action> toolBarButtonFactory;
    @Reference
    private ActionFactory<Action> actionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();
    private final ActionResolutionManager<ToolBarEntryDescriptor> actionResolutionManager = new ActionResolutionManager<>();
    private ServiceTracker<Action, ServiceReference<Action>> tracker;

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

    @Activate
    @Override
    protected void activate(ComponentContext context) {
        tracker = createActionTracker(context);
        tracker.open();
        getToolBarContainer().addToolBarContainerListener(new ToolBarContainerListenerAdapter<ToolBar, ToolBarButton>() {

            @Override
            public void toolBarAdded(ToolBarContainerToolBarEvent<ToolBar, ToolBarButton> event) {
                resolveUnresolvedToolBarEntries(event.getToolBarId());
            }
        });
        super.activate(context);
    }

    @Deactivate
    @Override
    protected void deactivate(ComponentContext context) {
        tracker.close();
        super.deactivate(context);
    }

    private ServiceTracker<Action, ServiceReference<Action>> createActionTracker(ComponentContext context) {
        return new ServiceTracker<>(context.getBundleContext(), actionFactory.getActionClass(),
                new ServiceTrackerCustomizer<Action, ServiceReference<Action>>() {

                    @Override
                    public ServiceReference<Action> addingService(ServiceReference<Action> reference) {
                        String actionId = actionRegistry.getActionId(reference);
                        if (actionResolutionManager.containsUnresolvedEntries(actionId)) {
                            for (UnresolvedEntry<ToolBarEntryDescriptor> unresolvedEntry :
                                    actionResolutionManager.removeUnresolvedEntries(actionId)) {
                                resolveToolBarEntry(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
                            }
                        }
                        return reference;
                    }

                    @Override
                    public void modifiedService(ServiceReference<Action> reference, ServiceReference<Action> service) {
                        // TODO ???
                    }

                    @Override
                    public void removedService(ServiceReference<Action> reference, ServiceReference<Action> service) {
                        // TODO ???
                    }
                });
    }

    @Override
    protected void resolveToolBarsType(ToolBarsType toolBarsType, Bundle bundle, BundleContext context) {
        for (ToolBarEntryType toolBarEntry : toolBarsType.getToolBarEntry()) {
            ToolBarEntryDescriptor toolBarEntryDescriptor = ToolBarEntryDescriptor.createToolBarEntryDescriptor(
                    toolBarEntry);
            resolveToolBarEntry(toolBarEntryDescriptor, context);
        }
    }

    private void resolveToolBarEntry(ToolBarEntryDescriptor toolBarEntryDescriptor, BundleContext context) {
        if (isInitialized() && getToolBarContainer().containsToolBar(toolBarEntryDescriptor.getToolBarId())) {
            System.out.println(actionFactory.getActionClass().getName() + ": " + toolBarEntryDescriptor.getActionId());
            Action action = actionRegistry.getAction(toolBarEntryDescriptor.getActionId(),
                    actionFactory.getActionClass(),
                    context);
            if (action != null) {
                ToolBarButton button = toolBarButtonFactory.createToolBarButton(toolBarEntryDescriptor, action,
                        ICON_SIZE);
                getToolBarContainer().addToolBarButton(toolBarEntryDescriptor.getToolBarId(),
                        new PositionableAdapter<>(button, toolBarEntryDescriptor.getPosition()));
            } else {
                actionResolutionManager.addUnresolvedEntry(toolBarEntryDescriptor.getActionId(),
                        new UnresolvedEntry<>(toolBarEntryDescriptor, context));
            }
        } else {
            registerUnresolvedToolBarEntry(toolBarEntryDescriptor, context);
        }
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && toolBarButtonFactory != null && actionFactory != null;
    }

    private void registerUnresolvedToolBarEntry(ToolBarEntryDescriptor toolBarEntryDescriptor, BundleContext context) {
        getToolBarResolutionManager().addUnresolvedToolBarEntry(new UnresolvedEntry<>(toolBarEntryDescriptor, context));
    }

    private void resolveUnresolvedToolBarEntries(String toolBarId) {
        if (getToolBarResolutionManager().containsUnresolvedToolBarEntries(toolBarId)) {
            for (UnresolvedEntry<ToolBarEntryDescriptor> unresolvedEntry : getToolBarResolutionManager().removeUnresolvedToolBarEntries(
                    toolBarId)) {
                resolveToolBarEntry(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
            }
        }
    }

    @Override
    protected void resolveUnresolvedItems() {
        for (UnresolvedEntry<ToolBarEntryDescriptor> unresolvedEntry : getToolBarResolutionManager().removeUnresolvedToolBarEntries()) {
            resolveToolBarEntry(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
        }
    }
}
