/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Deactivate;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.richclientplatform.core.action.spi.ActionRegistry;
import org.richclientplatform.core.action.spi.ToolBarContainerListenerAdapter;
import org.richclientplatform.core.action.spi.ToolBarContainerToolBarEvent;
import org.richclientplatform.core.action.spi.ToolBarEntryDescriptor;
import org.richclientplatform.core.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
public abstract class AbstractToolBarButtonHandler<ToolBar, ToolBarButton, Action, D extends ToolBarEntryDescriptor> extends AbstractToolBarHandler<ToolBar, ToolBarButton> {

    private static final int ICON_SIZE = 24;
    private final ToolBarEntryResolutionManager<D> toolBarEntryResolutionManager = new ToolBarEntryResolutionManager<>();
    private final ActionRegistry actionRegistry = new ActionRegistry();
    private final ActionResolutionManager<D> actionResolutionManager = new ActionResolutionManager<>();
    private ServiceTracker<Action, ServiceReference<Action>> tracker;

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
        return new ServiceTracker<>(context.getBundleContext(), getActionClass(),
                new ServiceTrackerCustomizer<Action, ServiceReference<Action>>() {

                    @Override
                    public ServiceReference<Action> addingService(ServiceReference<Action> reference) {
                        String actionId = actionRegistry.getActionId(reference);
                        if (actionResolutionManager.containsUnresolvedEntries(actionId)) {
                            for (UnresolvedEntry<D> unresolvedEntry :
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

    protected void resolveToolBarEntry(D toolBarEntryDescriptor, BundleContext context) {
        if (isInitialized() && getToolBarContainer().containsToolBar(toolBarEntryDescriptor.getToolBarId())) {
            System.out.println(getActionClass().getName() + ": " + toolBarEntryDescriptor.getActionId());
            Action action = actionRegistry.getAction(toolBarEntryDescriptor.getActionId(), getActionClass(), context);
            if (action != null) {
                ToolBarButton button = createToolBarButton(toolBarEntryDescriptor, action, ICON_SIZE);
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

    private void registerUnresolvedToolBarEntry(D toolBarEntryDescriptor, BundleContext context) {
        toolBarEntryResolutionManager.addUnresolvedToolBarEntry(new UnresolvedEntry<>(toolBarEntryDescriptor, context));
    }

    private void resolveUnresolvedToolBarEntries(String toolBarId) {
        if (toolBarEntryResolutionManager.containsUnresolvedToolBarEntries(toolBarId)) {
            for (UnresolvedEntry<D> unresolvedEntry : toolBarEntryResolutionManager.removeUnresolvedToolBarEntries(
                    toolBarId)) {
                resolveToolBarEntry(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
            }
        }
    }

    @Override
    protected void resolveUnresolvedItems() {
        for (UnresolvedEntry<D> unresolvedEntry : toolBarEntryResolutionManager.removeUnresolvedToolBarEntries()) {
            resolveToolBarEntry(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
        }
    }

    protected abstract Class<Action> getActionClass();

    protected abstract ToolBarButton createToolBarButton(D toolBarEntryDescriptor, Action action, int ICON_SIZE);
}
