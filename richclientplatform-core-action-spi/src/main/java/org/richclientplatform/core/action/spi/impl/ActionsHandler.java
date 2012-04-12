/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.action.jaxb.ActionType;
import org.richclientplatform.core.action.jaxb.ActionsType;
import org.richclientplatform.core.action.spi.ActionDescriptor;
import org.richclientplatform.core.action.spi.ActionFactory;
import org.richclientplatform.core.action.spi.ActionRegistry;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "actionsType", referenceInterface = ActionsType.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "actionDescriptor", referenceInterface = ActionDescriptor.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)})
public class ActionsHandler<T> {

    @Reference
    private ActionFactory<T> actionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();

    protected void bindActionsType(ServiceReference<ActionsType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        ActionsType actionsType = context.getService(serviceReference);
        for (ActionType actionType : actionsType.getAction()) {
            try {
                registerAction(actionType, bundle, context);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(ActionsHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    protected void unbindActionsType(ActionsType actionsType) {
        // TODO
    }

    protected void bindActionDescriptor(ServiceReference<ActionDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ActionDescriptor actionDescriptor = context.getService(serviceReference);
        registerAction(actionDescriptor, context);
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

    private void registerAction(ActionType actionType, Bundle bundle, BundleContext context)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ActionDescriptor actionDescriptor = ActionDescriptor.createActionDescriptor(actionType, bundle);
        registerAction(actionDescriptor, context);
    }

    private void registerAction(ActionDescriptor actionDescriptor, BundleContext context) {
        T action = actionFactory.createAction(actionDescriptor);
        actionRegistry.registerAction(actionDescriptor.getId(), actionFactory.getActionClass(), action, context);
    }
}
