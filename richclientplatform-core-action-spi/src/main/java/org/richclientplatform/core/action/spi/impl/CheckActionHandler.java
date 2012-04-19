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
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.action.jaxb.ActionType;
import org.richclientplatform.core.action.jaxb.ActionsType;
import org.richclientplatform.core.action.jaxb.CheckActionType;
import org.richclientplatform.core.action.spi.ActionDescriptor;
import org.richclientplatform.core.action.spi.ActionFactory;
import org.richclientplatform.core.action.spi.ActionRegistry;
import org.richclientplatform.core.action.spi.CheckActionDescriptor;
import org.richclientplatform.core.action.spi.CheckActionFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "checkActionDescriptor", referenceInterface = CheckActionDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class CheckActionHandler<T> extends AbstractActionHandler {

    @Reference
    private CheckActionFactory<T> actionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();

    protected void bindCheckActionDescriptor(ServiceReference<CheckActionDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        CheckActionDescriptor actionDescriptor = context.getService(serviceReference);
        registerCheckAction(actionDescriptor, context);
    }

    protected void unbindCheckActionDescriptor(CheckActionDescriptor actionDescriptor) {
        // TODO
    }

    protected void bindCheckActionFactory(CheckActionFactory<T> actionFactory) {
        this.actionFactory = actionFactory;
    }

    protected void unbindCheckActionFactory(CheckActionFactory<T> actionFactory) {
        this.actionFactory = null;
    }

    @Override
    protected void registerAction(ActionsType actionsType, Bundle bundle, BundleContext context) {
        for (CheckActionType actionType : actionsType.getCheckAction()) {
            try {
                registerCheckAction(actionType, bundle, context);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(CheckActionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void registerCheckAction(CheckActionType actionType, Bundle bundle, BundleContext context)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        CheckActionDescriptor actionDescriptor = CheckActionDescriptor.createCheckActionDescriptor(actionType, bundle);
        registerCheckAction(actionDescriptor, context);
    }

    private void registerCheckAction(ActionDescriptor actionDescriptor, BundleContext context) {
        T action = actionFactory.createCheckAction(actionDescriptor);
        actionRegistry.registerAction(actionDescriptor.getId(), actionFactory.getCheckActionClass(), action, context);
    }
}
