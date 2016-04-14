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
import org.drombler.acp.core.action.jaxb.ActionsType;
import org.drombler.acp.core.action.jaxb.ToggleActionType;
import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.ToggleActionDescriptor;
import org.drombler.acp.core.action.spi.ToggleActionFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "toggleActionDescriptor", referenceInterface = ToggleActionDescriptor.class,
        cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class ToggleActionHandler<T> extends AbstractActionHandler<ToggleActionType, ToggleActionDescriptor<?>> {

    @Reference
    private ToggleActionFactory<T> toggleActionFactory;

    protected void bindToggleActionDescriptor(ServiceReference<ToggleActionDescriptor<?>> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ToggleActionDescriptor<?> actionDescriptor = context.getService(serviceReference);
        registerActionDescriptor(actionDescriptor, context);
    }

    protected void unbindToggleActionDescriptor(ToggleActionDescriptor<?> actionDescriptor) {
        // TODO
    }

    protected void bindToggleActionFactory(ToggleActionFactory<T> toggleActionFactory) {
        this.toggleActionFactory = toggleActionFactory;
    }

    protected void unbindToggleActionFactory(ToggleActionFactory<T> toggleActionFactory) {
        this.toggleActionFactory = null;
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
            T action = toggleActionFactory.createToggleAction(actionDescriptor);
            registerActionListener(context, actionDescriptor);
            getActionRegistry().registerAction(actionDescriptor.getId(), toggleActionFactory.getToggleActionClass(),
                    action, context);
        } else {
            registerUnresolvedActionDescriptor(actionDescriptor, context);
        }
    }

    private <T> void registerActionListener(BundleContext context, ActionDescriptor<T> actionDescriptor) {
        context.registerService(actionDescriptor.getListenerType(), actionDescriptor.getListener(), null);
    }

    @Override
    protected ToggleActionDescriptor<?> createActionDescriptor(ToggleActionType actionType, BundleContext context)
            throws
            IllegalAccessException, ClassNotFoundException, InstantiationException {
        return ToggleActionDescriptor.
                createToggleActionDescriptor(actionType, context.getBundle(), getContextInjector());
    }
}
