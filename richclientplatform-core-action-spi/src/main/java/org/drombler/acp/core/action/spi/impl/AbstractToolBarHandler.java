/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.drombler.acp.core.action.jaxb.ToolBarsType;
import org.drombler.acp.core.action.spi.ApplicationToolBarContainerProvider;
import org.drombler.acp.core.action.spi.ToolBarContainer;

/**
 *
 * @author puce
 */
@References({
    @Reference(name = "toolBarsType", referenceInterface = ToolBarsType.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
//    @Reference(name = "menuDescriptor", referenceInterface = MenuDescriptor.class,
//    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
//    @Reference(name = "menuEntryDescriptor", referenceInterface = MenuEntryDescriptor.class,
//    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "applicationToolBarContainerProvider", referenceInterface = ApplicationToolBarContainerProvider.class)
})
public abstract class AbstractToolBarHandler<T, B> {

    private ToolBarContainer<T, B> toolBarContainer;
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

    protected void bindToolBarsType(ServiceReference<ToolBarsType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        ToolBarsType toolBarsType = context.getService(serviceReference);

        resolveToolBarsType(toolBarsType, bundle, context);
    }

    protected void unbindToolBarsType(ToolBarsType toolBarsType) {
        // TODO
    }

    protected void bindApplicationToolBarContainerProvider(ApplicationToolBarContainerProvider<T, B> applicationToolBarContainerProvider) {
        toolBarContainer = applicationToolBarContainerProvider.getApplicationToolBarContainer();
    }

    protected void unbindApplicationToolBarContainerProvider(ApplicationToolBarContainerProvider<T, B> menuBarMenuContainerProvider) {
        toolBarContainer = null;
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedItems();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    protected abstract void resolveToolBarsType(ToolBarsType toolBarsType, Bundle bundle, BundleContext context);

    protected abstract void resolveUnresolvedItems();

    protected boolean isInitialized() {
        return toolBarContainer != null;
    }

    protected ToolBarContainer<T, B> getToolBarContainer() {
        return toolBarContainer;
    }
}
