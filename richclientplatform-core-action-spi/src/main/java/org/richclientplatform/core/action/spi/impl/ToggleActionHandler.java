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
import org.richclientplatform.core.action.jaxb.ActionsType;
import org.richclientplatform.core.action.jaxb.CheckActionType;
import org.richclientplatform.core.action.jaxb.ToggleActionType;
import org.richclientplatform.core.action.spi.ActionDescriptor;
import org.richclientplatform.core.action.spi.ActionRegistry;
import org.richclientplatform.core.action.spi.CheckActionDescriptor;
import org.richclientplatform.core.action.spi.CheckActionFactory;
import org.richclientplatform.core.action.spi.ToggleActionDescriptor;
import org.richclientplatform.core.action.spi.ToggleActionFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "toggleActionDescriptor", referenceInterface = ToggleActionDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class ToggleActionHandler<T> extends AbstractActionHandler {

    @Reference
    private ToggleActionFactory<T> actionFactory;
    private final ActionRegistry actionRegistry = new ActionRegistry();

    protected void bindToggleActionDescriptor(ServiceReference<ToggleActionDescriptor> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ToggleActionDescriptor actionDescriptor = context.getService(serviceReference);
        registerToggleAction(actionDescriptor, context);
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

    @Override
    protected void registerAction(ActionsType actionsType, Bundle bundle, BundleContext context) {
        for (ToggleActionType actionType : actionsType.getToggleAction()) {
            try {
                registerToggleAction(actionType, bundle, context);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(ToggleActionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void registerToggleAction(ToggleActionType actionType, Bundle bundle, BundleContext context)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ToggleActionDescriptor actionDescriptor = ToggleActionDescriptor.createToggleActionDescriptor(actionType, bundle);
        registerToggleAction(actionDescriptor, context);
    }

    private void registerToggleAction(ToggleActionDescriptor actionDescriptor, BundleContext context) {
        T action = actionFactory.createToggleAction(actionDescriptor);
        actionRegistry.registerAction(actionDescriptor.getId(), actionFactory.getToggleActionClass(), action, context);
    }
}
