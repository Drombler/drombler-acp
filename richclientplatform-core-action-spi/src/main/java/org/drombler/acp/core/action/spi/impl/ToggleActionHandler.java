/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.drombler.acp.core.action.jaxb.ActionsType;
import org.drombler.acp.core.action.jaxb.ToggleActionType;
import org.drombler.acp.core.action.spi.ToggleActionDescriptor;
import org.drombler.acp.core.action.spi.ToggleActionFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "toggleActionDescriptor", referenceInterface = ToggleActionDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class ToggleActionHandler<T> extends AbstractActionHandler<ToggleActionType, ToggleActionDescriptor> {

    @Reference
    private ToggleActionFactory<T> actionFactory;

    protected void bindToggleActionDescriptor(ServiceReference<ToggleActionDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ToggleActionDescriptor actionDescriptor = context.getService(serviceReference);
        registerActionDescriptor(actionDescriptor, context);
    }

    protected void unbindToggleActionDescriptor(ToggleActionDescriptor actionDescriptor) {
        // TODO
    }

    protected void bindToggleActionFactory(ToggleActionFactory<T> actionFactory) {
        this.actionFactory = actionFactory;
    }

    protected void unbindToggleActionFactory(ToggleActionFactory<T> actionFactory) {
        this.actionFactory = null;
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedItems();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && actionFactory != null;
    }

    @Override
    protected void registerActions(ActionsType actionsType, BundleContext context) {
        for (ToggleActionType actionType : actionsType.getToggleAction()) {
            registerActionType(actionType, context);
        }
    }

    @Override
    protected void registerActionDescriptor(ToggleActionDescriptor actionDescriptor, BundleContext context) {
        if (isInitialized()) {
            T action = actionFactory.createToggleAction(actionDescriptor);
            getActionRegistry().registerAction(actionDescriptor.getId(), actionFactory.getToggleActionClass(), action,
                    context);
        } else {
            registerUnresolvedActionDescriptor(actionDescriptor, context);
        }
    }

    @Override
    protected ToggleActionDescriptor createActionDescriptor(ToggleActionType actionType, BundleContext context) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        return ToggleActionDescriptor.createToggleActionDescriptor(actionType, context.getBundle(),
                getActiveContextProvider().getActiveContext(), getApplicationContextProvider().getApplicationContext());
    }
}
