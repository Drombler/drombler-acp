/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.concurrent.Executor;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.richclientplatform.core.action.jaxb.ToolBarType;
import org.richclientplatform.core.action.jaxb.ToolBarsType;
import org.richclientplatform.core.action.spi.ToggleActionDescriptor;
import org.richclientplatform.core.action.spi.ToggleMenuEntryDescriptor;
import org.richclientplatform.core.action.spi.ToolBarDescriptor;
import org.richclientplatform.core.action.spi.ToolBarFactory;
import org.richclientplatform.core.application.ApplicationExecutorProvider;
import org.richclientplatform.core.lib.util.PositionableAdapter;

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
