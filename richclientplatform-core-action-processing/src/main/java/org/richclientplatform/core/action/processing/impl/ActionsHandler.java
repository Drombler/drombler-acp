/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.processing.impl;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.action.processing.ActionDescriptor;
import org.richclientplatform.core.action.jaxb.ActionsType;
import org.richclientplatform.core.action.jaxb.ActionType;
import org.richclientplatform.core.action.processing.ActionFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "actionsType", referenceInterface = ActionsType.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class ActionsHandler {

    @Reference
    private ActionFactory<?> actionFactory;

    protected void bindActionsType(ServiceReference<ActionsType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        ActionsType actionsType = context.getService(serviceReference);
        for (ActionType actionType : actionsType.getAction()) {
            try {
                registerAction(actionType, actionFactory, bundle, context);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(ActionsHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private <T> void registerAction(ActionType actionType, ActionFactory<T> actionFactory, Bundle bundle, BundleContext context)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ActionDescriptor actionDescriptor = ActionDescriptor.createActionDescriptor(actionType, bundle);
        T action = actionFactory.createAction(actionDescriptor);
        Dictionary<String, String> properties = new Hashtable<>(1);
        properties.put(ActionDescriptor.ID_KEY, actionDescriptor.getId());
        context.registerService(actionFactory.getActionClass(), action, properties);
    }

    protected void unbindActionsType(ActionsType actionsType) {
        // TODO
    }

    protected void bindActionFactory(ActionFactory<?> actionFactory) {
        this.actionFactory = actionFactory;
    }

    protected void unbindActionFactory(ActionFactory<?> actionFactory) {
        this.actionFactory = null;
    }
}
