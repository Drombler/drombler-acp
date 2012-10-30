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
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.drombler.acp.core.action.jaxb.ToolBarType;
import org.drombler.acp.core.action.jaxb.ToolBarsType;
import org.drombler.acp.core.action.spi.ToggleActionDescriptor;
import org.drombler.acp.core.action.spi.ToggleMenuEntryDescriptor;
import org.drombler.acp.core.action.spi.ToolBarDescriptor;
import org.drombler.acp.core.action.spi.ToolBarFactory;
import org.drombler.acp.core.application.ApplicationExecutorProvider;
import org.drombler.acp.core.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "applicationExecutorProvider", referenceInterface = ApplicationExecutorProvider.class)
public class ToolBarsHandler<T, B> extends AbstractToolBarHandler<T, B> {

    private final ToolBarResolutionManager toolBarResolutionManager = new ToolBarResolutionManager();
    @Reference
    private ToolBarFactory<T> toolBarFactory;
    private Executor applicationExecutor;

    protected void bindToolBarFactory(ToolBarFactory<T> toolBarFactory) {
        this.toolBarFactory = toolBarFactory;
    }

    protected void unbindToolBarFactory(ToolBarFactory<T> toolBarFactory) {
        this.toolBarFactory = null;
    }

    protected void bindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = applicationExecutorProvider.getApplicationExecutor();
    }

    protected void unbindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = null;
    }

    @Activate
    @Override
    protected void activate(ComponentContext context) {
        super.activate(context);
    }

    @Deactivate
    @Override
    protected void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    @Override
    protected void resolveToolBarsType(ToolBarsType toolBarsType, Bundle bundle, BundleContext context) {
        for (ToolBarType toolBarType : toolBarsType.getToolBar()) {
            resolveToolBar(toolBarType, bundle, context);
        }
    }

    private void resolveToolBar(ToolBarType toolBarType, Bundle bundle, BundleContext context) {
        if (isInitialized()) {
            ToolBarDescriptor toolBarDescriptor = ToolBarDescriptor.createToolBarDescriptor(toolBarType, bundle,
                    getToolBarContainer());
            resolveToolBar(toolBarDescriptor, context);
        } else {
            toolBarResolutionManager.addUnresolvedToolBarType(new UnresolvedEntry<>(toolBarType, context));
        }
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && toolBarFactory != null && applicationExecutor != null;
    }

    protected void resolveToolBar(final ToolBarDescriptor toolBarDescriptor, final BundleContext context) {
        if (isInitialized()) {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    T toolBar = toolBarFactory.createToolBar(toolBarDescriptor);
                    getToolBarContainer().addToolBar(toolBarDescriptor.getId(),
                            new PositionableAdapter<>(toolBar, toolBarDescriptor.getPosition()));
                    getToolBarContainer().setToolBarVisible(toolBarDescriptor.getId(), toolBarDescriptor.isVisible());
                    context.registerService(ToggleActionDescriptor.class,
                            toolBarDescriptor.getShowToolBarActionDescriptor(),
                            null);
                    context.registerService(ToggleMenuEntryDescriptor.class,
                            toolBarDescriptor.getShowToolBarCheckMenuEntryDescriptor(), null);
                }
            };
            applicationExecutor.execute(runnable);
        } else {
            registerUnresolvedToolBar(toolBarDescriptor, context);
        }
    }

    private void registerUnresolvedToolBar(ToolBarDescriptor toolBarDescriptor, BundleContext context) {
        toolBarResolutionManager.addUnresolvedToolBar(new UnresolvedEntry<>(toolBarDescriptor, context));
    }

    @Override
    protected void resolveUnresolvedItems() {
        if (isInitialized()) {
            for (UnresolvedEntry<ToolBarType> unresolvedEntry : toolBarResolutionManager.removeUnresolvedToolBarTypes()) {
                resolveToolBar(unresolvedEntry.getEntry(), unresolvedEntry.getContext().getBundle(),
                        unresolvedEntry.getContext());
            }
            for (UnresolvedEntry<ToolBarDescriptor> toolBarDescriptor : toolBarResolutionManager.removeUnresolvedToolBars()) {
                resolveToolBar(toolBarDescriptor.getEntry(), toolBarDescriptor.getContext());
            }
        }
    }
}
