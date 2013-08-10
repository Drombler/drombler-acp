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

import java.util.concurrent.Executor;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.drombler.acp.core.action.spi.ActionRegistry;
import org.drombler.acp.core.action.spi.ToolBarContainerListenerAdapter;
import org.drombler.acp.core.action.spi.ToolBarContainerToolBarEvent;
import org.drombler.acp.core.action.spi.ToolBarEntryDescriptor;
import org.drombler.acp.core.application.ApplicationExecutorProvider;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.softsmithy.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
@Reference(name = "applicationExecutorProvider", referenceInterface = ApplicationExecutorProvider.class)
public abstract class AbstractToolBarButtonHandler<ToolBar, ToolBarButton, Action, D extends ToolBarEntryDescriptor> extends AbstractToolBarHandler<ToolBar, ToolBarButton> {

    private static final int ICON_SIZE = 24;
    private final ToolBarEntryResolutionManager<D> toolBarEntryResolutionManager = new ToolBarEntryResolutionManager<>();
    private final ActionRegistry actionRegistry = new ActionRegistry();
    private final ActionResolutionManager<D> actionResolutionManager = new ActionResolutionManager<>();
    private Executor applicationExecutor;
    private ServiceTracker<Action, ServiceReference<Action>> tracker;

    protected void bindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = applicationExecutorProvider.getApplicationExecutor();
    }

    protected void unbindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = null;
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

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && applicationExecutor != null;
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

    protected void resolveToolBarEntry(final D toolBarEntryDescriptor, BundleContext context) {
        if (isInitialized() && getToolBarContainer().containsToolBar(toolBarEntryDescriptor.getToolBarId())) {
            final Action action = actionRegistry.getAction(toolBarEntryDescriptor.getActionId(), getActionClass(),
                    context);
            if (action != null) {
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        ToolBarButton button = createToolBarButton(toolBarEntryDescriptor, action, ICON_SIZE);
                        getToolBarContainer().addToolBarButton(toolBarEntryDescriptor.getToolBarId(),
                                new PositionableAdapter<>(button, toolBarEntryDescriptor.getPosition()));
                    }
                };
                applicationExecutor.execute(runnable);
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
