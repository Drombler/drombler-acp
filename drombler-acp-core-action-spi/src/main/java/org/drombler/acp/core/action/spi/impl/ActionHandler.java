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

import org.drombler.acp.core.action.jaxb.ActionType;
import org.drombler.acp.core.action.jaxb.ActionsType;
import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.ActionFactory;
import org.drombler.acp.core.action.spi.ActionRegistry;
import org.drombler.commons.context.ContextInjector;
import org.drombler.commons.context.ContextManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 *
 * @author puce
 */
@Component(immediate = true)
public class ActionHandler<T> extends AbstractActionHandler<ActionType, ActionDescriptor<?>> {

    @Reference
    private ActionFactory<T> actionFactory;

    private final ActionRegistry<ActionDescriptor<?>> actionRegistry = new ActionRegistry<>((Class<ActionDescriptor<?>>) (Class<?>) ActionDescriptor.class);

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindActionDescriptor(ServiceReference<ActionDescriptor<?>> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ActionDescriptor<?> actionDescriptor = context.getService(serviceReference);
        registerActionDescriptor(actionDescriptor, context);
    }

    protected void unbindActionDescriptor(ActionDescriptor<?> actionDescriptor) {
        unregisterActionDescriptor(actionDescriptor);
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
    protected ActionDescriptor<?> createActionDescriptor(ActionType actionType, BundleContext context, ContextManager contextManager, ContextInjector contextInjector)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        return ActionDescriptor.createActionDescriptor(actionType, context.getBundle(), contextManager, contextInjector);
    }

    @Override
    protected ActionRegistry<ActionDescriptor<?>> getActionRegistry() {
        return actionRegistry;
    }

    @Override
    protected void registerActionDescriptor(ActionDescriptor<?> actionDescriptor, BundleContext context) {
        if (isInitialized()) {
            registerActionDescriptor(actionDescriptor);
            T action = actionFactory.createAction(actionDescriptor);
            if (!actionDescriptor.getListenerType().equals(actionFactory.getActionClass())) {
                registerActionListener(context, actionDescriptor);
            }
            getActionRegistry().registerAction(actionDescriptor.getId(), actionFactory.getActionClass(), action, context);
        } else {
            registerUnresolvedActionDescriptor(actionDescriptor, context);
        }
    }

    //    TODO [puce, 18.04.2017]: used? If yes, use getActionRegistry().registerAction instead (set the id property)?
    private <T> void registerActionListener(BundleContext context, ActionDescriptor<T> actionDescriptor) {
        context.registerService(actionDescriptor.getListenerType(), actionDescriptor.getListener(), null);
    }
}
