/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.action.jaxb.ToolBarEntryType;
import org.richclientplatform.core.action.jaxb.ToolBarType;
import org.richclientplatform.core.action.jaxb.ToolBarsType;
import org.richclientplatform.core.action.spi.ActionFactory;
import org.richclientplatform.core.action.spi.ActionRegistry;
import org.richclientplatform.core.action.spi.ApplicationToolBarContainerProvider;
import org.richclientplatform.core.action.spi.ToggleActionDescriptor;
import org.richclientplatform.core.action.spi.ToggleMenuEntryDescriptor;
import org.richclientplatform.core.action.spi.ToolBarButtonFactory;
import org.richclientplatform.core.action.spi.ToolBarContainer;
import org.richclientplatform.core.action.spi.ToolBarDescriptor;
import org.richclientplatform.core.action.spi.ToolBarEntryDescriptor;
import org.richclientplatform.core.action.spi.ToolBarFactory;
import org.richclientplatform.core.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "toolBarsType", referenceInterface = ToolBarsType.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
//    @Reference(name = "menuDescriptor", referenceInterface = MenuDescriptor.class,
//    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
//    @Reference(name = "menuEntryDescriptor", referenceInterface = MenuEntryDescriptor.class,
//    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "applicationToolBarContainerProvider", referenceInterface = ApplicationToolBarContainerProvider.class)
})
public class ToolBarsHandler<T, B, A> {

    private static final int ICON_SIZE = 24;
    private final ToolBarResolutionManager toolBarResolutionManager = new ToolBarResolutionManager();
    private ToolBarContainer<T, B> toolBarContainer;
    @Reference
    private ToolBarFactory<T> toolBarFactory;
    @Reference
    private ToolBarButtonFactory<B, A> toolBarButtonFactory;
    @Reference
    private ActionFactory<A> actionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();

    protected void bindToolBarsType(ServiceReference<ToolBarsType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        ToolBarsType toolBarsType = context.getService(serviceReference);

        for (ToolBarType toolBarType : toolBarsType.getToolBar()) {
            ToolBarDescriptor toolBarDescriptor = ToolBarDescriptor.createToolBarDescriptor(toolBarType, bundle,
                    toolBarContainer);
            resolveToolBar(toolBarDescriptor, context);
        }

        for (ToolBarEntryType toolBarEntry : toolBarsType.getToolBarEntry()) {
            ToolBarEntryDescriptor toolBarEntryDescriptor = ToolBarEntryDescriptor.createToolBarEntryDescriptor(
                    toolBarEntry);
            resolveToolBarEntry(toolBarEntryDescriptor, context);
        }

    }

    protected void unbindToolBarsType(ToolBarsType toolBarsType) {
        // TODO
    }

//    protected void bindMenuDescriptor(MenuDescriptor menuDescriptor) {
//        resolveMenu(menuDescriptor);
//    }
//
//    protected void unbindMenuDescriptor(MenuDescriptor menuDescriptor) {
//        // TODO
//    }
//
//    protected void bindMenuEntryDescriptor(ServiceReference<MenuEntryDescriptor> serviceReference) {
//        BundleContext context = serviceReference.getBundle().getBundleContext();
//        MenuEntryDescriptor menuEntryDescriptor = context.getService(serviceReference);
//        resolveMenuItem(menuEntryDescriptor, context);
//    }
//
//    protected void unbindMenuEntryDescriptor(ServiceReference<MenuEntryDescriptor> serviceReference) {
//        // TODO
//    }
    protected void bindApplicationToolBarContainerProvider(ApplicationToolBarContainerProvider<T, B> applicationToolBarContainerProvider) {
        toolBarContainer = applicationToolBarContainerProvider.getApplicationToolBarContainer();
        resolveUnresolvedItems();
    }

    protected void unbindApplicationToolBarContainerProvider(ApplicationToolBarContainerProvider<T, B> menuBarMenuContainerProvider) {
        toolBarContainer = null;
    }

    protected void bindToolBarFactory(ToolBarFactory<T> toolBarFactory) {
        this.toolBarFactory = toolBarFactory;
        resolveUnresolvedItems();
    }

    protected void unbindToolBarFactory(ToolBarFactory<T> toolBarFactory) {
        this.toolBarFactory = null;
    }

    protected void bindToolBarButtonFactory(ToolBarButtonFactory<B, A> toolBarButtonFactory) {
        this.toolBarButtonFactory = toolBarButtonFactory;
        resolveUnresolvedItems();
    }

    protected void unbindToolBarButtonFactory(ToolBarButtonFactory<B, A> toolBarButtonFactory) {
        this.toolBarButtonFactory = null;
    }

    protected void bindActionFactory(ActionFactory<A> actionFactory) {
        this.actionFactory = actionFactory;
        resolveUnresolvedItems();
    }

    protected void unbindActionFactory(ActionFactory<A> actionFactory) {
        this.actionFactory = null;
    }

    private void resolveToolBar(ToolBarDescriptor toolBarDescriptor, BundleContext context) {
        if (isInitialized()) {
            T toolBar = toolBarFactory.createToolBar(toolBarDescriptor);
            toolBarContainer.addToolBar(toolBarDescriptor.getId(),
                    new PositionableAdapter<>(toolBar, toolBarDescriptor.getPosition()));
            toolBarContainer.setToolBarVisible(toolBarDescriptor.getId(), toolBarDescriptor.isVisible());
            context.registerService(ToggleActionDescriptor.class, toolBarDescriptor.getShowToolBarActionDescriptor(),
                    null);
            context.registerService(ToggleMenuEntryDescriptor.class,
                    toolBarDescriptor.getShowToolBarCheckMenuEntryDescriptor(), null);
            resolveUnresolvedToolBarEntries(toolBarDescriptor.getId());
        } else {
            registerUnresolvedToolBar(toolBarDescriptor, context);
        }
    }

    private boolean isInitialized() {
        return toolBarContainer != null && toolBarFactory != null && toolBarButtonFactory != null && actionFactory != null;
    }

    private void resolveToolBarEntry(ToolBarEntryDescriptor toolBarEntryDescriptor, BundleContext context) {
        if (isInitialized()) {
            A action = actionRegistry.getAction(toolBarEntryDescriptor.getActionId(), actionFactory.getActionClass(),
                    context);
            if (action != null) {
                B button = toolBarButtonFactory.createToolBarButton(toolBarEntryDescriptor, action, ICON_SIZE);
                toolBarContainer.addToolBarButton(toolBarEntryDescriptor.getToolBarId(),
                        new PositionableAdapter<>(button, toolBarEntryDescriptor.getPosition()));
            }
        } else {
            registerUnresolvedToolBarEntry(toolBarEntryDescriptor, context);
        }
    }

    private void registerUnresolvedToolBar(ToolBarDescriptor toolBarDescriptor, BundleContext context) {
        toolBarResolutionManager.addUnresolvedToolBar(new UnresolvedEntry<>(toolBarDescriptor, context));
    }

    private void resolveUnresolvedItems() {
        if (isInitialized()) {
            for (UnresolvedEntry<ToolBarDescriptor> toolBarDescriptor : toolBarResolutionManager.removeUnresolvedToolBars()) {
                resolveToolBar(toolBarDescriptor.getEntry(), toolBarDescriptor.getContext());
            }
        }
    }

    private void registerUnresolvedToolBarEntry(ToolBarEntryDescriptor toolBarEntryDescriptor, BundleContext context) {
        toolBarResolutionManager.addUnresolvedToolBarEntry(new UnresolvedEntry<>(toolBarEntryDescriptor, context));
    }

    private void resolveUnresolvedToolBarEntries(String toolBarId) {
        if (toolBarResolutionManager.containsUnresolvedMenuEntries(toolBarId)) {
            for (UnresolvedEntry<ToolBarEntryDescriptor> unresolvedEntry : toolBarResolutionManager.removeUnresolvedToolBarEntries(
                    toolBarId)) {
                registerUnresolvedToolBarEntry(unresolvedEntry.getEntry(), unresolvedEntry.getContext());
            }
        }
    }
}
