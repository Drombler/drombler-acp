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

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.drombler.acp.core.action.jaxb.ActionType;
import org.drombler.acp.core.action.jaxb.ActionsType;
import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.ActionFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "actionDescriptor", referenceInterface = ActionDescriptor.class,
        cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class ActionHandler<T> extends AbstractActionHandler<ActionType, ActionDescriptor<?>> {

    @Reference
    private ActionFactory<T> actionFactory;

    protected void bindActionDescriptor(ServiceReference<ActionDescriptor<?>> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ActionDescriptor<?> actionDescriptor = context.getService(serviceReference);
        registerActionDescriptor(actionDescriptor, context);
    }

    protected void unbindActionDescriptor(ActionDescriptor<?> actionDescriptor) {
        // TODO
    }

    protected void bindActionFactory(ActionFactory<T> actionFactory) {
        this.actionFactory = actionFactory;
    }

    protected void unbindActionFactory(ActionFactory<T> actionFactory) {
        this.actionFactory = null;
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
    protected boolean isInitialized() {
        return super.isInitialized() && actionFactory != null;
    }

    @Override
    protected void registerActions(ActionsType actionsType, BundleContext context) {
        actionsType.getAction().forEach(actionType -> registerActionType(actionType, context));
    }

    @Override
    protected ActionDescriptor<?> createActionDescriptor(ActionType actionType, BundleContext context) throws
            IllegalAccessException, ClassNotFoundException, InstantiationException {
        return ActionDescriptor.createActionDescriptor(actionType, context.getBundle(), getContextInjector());
    }

    @Override
    protected void registerActionDescriptor(ActionDescriptor<?> actionDescriptor, BundleContext context) {
        if (isInitialized()) {
            T action = actionFactory.createAction(actionDescriptor);
            registerActionListener(context, actionDescriptor);
            getActionRegistry().
                    registerAction(actionDescriptor.getId(), actionFactory.getActionClass(), action, context);
        } else {
            registerUnresolvedActionDescriptor(actionDescriptor, context);
        }
    }

    private <T> void registerActionListener(BundleContext context, ActionDescriptor<T> actionDescriptor) {
        context.registerService(actionDescriptor.getListenerType(), actionDescriptor.getListener(), null);
    }
}
