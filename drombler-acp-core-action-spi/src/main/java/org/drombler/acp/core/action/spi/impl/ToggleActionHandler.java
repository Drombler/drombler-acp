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

import org.drombler.acp.core.action.jaxb.ActionsType;
import org.drombler.acp.core.action.jaxb.ToggleActionType;
import org.drombler.acp.core.action.spi.ActionRegistry;
import org.drombler.acp.core.action.spi.ToggleActionDescriptor;
import org.drombler.acp.core.action.spi.ToggleActionFactory;
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
public class ToggleActionHandler<T> extends AbstractActionHandler<ToggleActionType, ToggleActionDescriptor<?>> {

    @Reference
    private ToggleActionFactory<T> toggleActionFactory;

    private final ActionRegistry<ToggleActionDescriptor<?>> actionRegistry = new ActionRegistry<>((Class<ToggleActionDescriptor<?>>) (Class<?>) ToggleActionDescriptor.class);

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindToggleActionDescriptor(ServiceReference<ToggleActionDescriptor<?>> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ToggleActionDescriptor<?> actionDescriptor = context.getService(serviceReference);
        registerActionDescriptor(actionDescriptor, context);
    }

    protected void unbindToggleActionDescriptor(ToggleActionDescriptor<?> actionDescriptor) {
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
        return super.isInitialized() && toggleActionFactory != null;
    }

    @Override
    protected void registerActions(ActionsType actionsType, BundleContext context) {
        actionsType.getToggleAction().forEach((actionType) -> registerActionType(actionType, context));
    }

    @Override
    protected void registerActionDescriptor(ToggleActionDescriptor<?> actionDescriptor, BundleContext context) {
        if (isInitialized()) {
            registerActionDescriptor(actionDescriptor);
            T action = toggleActionFactory.createToggleAction(actionDescriptor);
            if (!actionDescriptor.getListenerType().equals(toggleActionFactory.getToggleActionClass())) {
                registerActionListener(context, actionDescriptor);
            }
            getActionRegistry().registerAction(actionDescriptor.getId(), toggleActionFactory.getToggleActionClass(),
                    action, context);
        } else {
            registerUnresolvedActionDescriptor(actionDescriptor, context);
        }
    }

    //    TODO [puce, 18.04.2017]: used? If yes, use getActionRegistry().registerAction instead (set the id property)?
    private <T> void registerActionListener(BundleContext context, ToggleActionDescriptor<T> actionDescriptor) {
        context.registerService(actionDescriptor.getListenerType(), actionDescriptor.getListener(), null);
    }

    @Override
    protected ToggleActionDescriptor<?> createActionDescriptor(ToggleActionType actionType, BundleContext context, ContextManager contextManager, ContextInjector contextInjector)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        return ToggleActionDescriptor.createToggleActionDescriptor(actionType, context.getBundle(), contextManager, contextInjector);
    }

    @Override
    protected ActionRegistry<ToggleActionDescriptor<?>> getActionRegistry() {
        return actionRegistry;
    }
}
