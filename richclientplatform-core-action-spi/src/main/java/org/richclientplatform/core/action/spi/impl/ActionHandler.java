/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.richclientplatform.core.action.jaxb.ActionType;
import org.richclientplatform.core.action.jaxb.ActionsType;
import org.richclientplatform.core.action.spi.ActionDescriptor;
import org.richclientplatform.core.action.spi.ActionFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "actionDescriptor", referenceInterface = ActionDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class ActionHandler<T> extends AbstractActionHandler<ActionType, ActionDescriptor> {

    @Reference
    private ActionFactory<T> actionFactory;

    protected void bindActionDescriptor(ServiceReference<ActionDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ActionDescriptor actionDescriptor = context.getService(serviceReference);
        registerActionDescriptor(actionDescriptor, context);
    }

    protected void unbindActionDescriptor(ActionDescriptor actionDescriptor) {
        // TODO
    }

    protected void bindActionFactory(ActionFactory<T> actionFactory) {
        this.actionFactory = actionFactory;
    }

    protected void unbindActionFactory(ActionFactory<T> actionFactory) {
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
        for (ActionType actionType : actionsType.getAction()) {
            registerActionType(actionType, context);
        }
    }

    @Override
    protected ActionDescriptor createActionDescriptor(ActionType actionType, BundleContext context) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        return ActionDescriptor.createActionDescriptor(actionType, context.getBundle(),
                getActiveContextProvider().getActiveContext(), getApplicationContextProvider().getApplicationContext());
    }

    @Override
    protected void registerActionDescriptor(ActionDescriptor actionDescriptor, BundleContext context) {
        if (isInitialized()) {
            T action = actionFactory.createAction(actionDescriptor);
            getActionRegistry().registerAction(actionDescriptor.getId(), actionFactory.getActionClass(), action, context);
        } else {
            registerUnresolvedActionDescriptor(actionDescriptor, context);
        }
    }
}
