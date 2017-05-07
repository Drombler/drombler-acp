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

import java.util.ArrayList;
import java.util.List;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.drombler.acp.core.action.jaxb.ActionsType;
import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.ActionRegistry;
import org.drombler.acp.core.commons.util.UnresolvedEntry;
import org.drombler.commons.context.ActiveContextProvider;
import org.drombler.commons.context.ApplicationContextProvider;
import org.drombler.commons.context.ContextInjector;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
@References({
    @Reference(name = "actionsType", referenceInterface = ActionsType.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
})
public abstract class AbstractActionHandler<A, D extends ActionDescriptor<?>> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractActionHandler.class);

    @Reference
    private ActiveContextProvider activeContextProvider;
    @Reference
    private ApplicationContextProvider applicationContextProvider;
    private ContextInjector contextInjector;
    private final List<D> actionDescriptors = new ArrayList<>();
    private final List<UnresolvedEntry<A>> unresolvedActions = new ArrayList<>();
    private final List<UnresolvedEntry<D>> unresolvedActionDescriptors = new ArrayList<>();

    protected void bindActiveContextProvider(ActiveContextProvider activeContextProvider) {
        this.activeContextProvider = activeContextProvider;
    }

    protected void unbindActiveContextProvider(ActiveContextProvider activeContextProvider) {
        this.activeContextProvider = null;
    }

    protected void bindApplicationContextProvider(ApplicationContextProvider applicationContextProvider) {
        this.applicationContextProvider = applicationContextProvider;
    }

    protected void unbindApplicationContextProvider(ApplicationContextProvider applicationContextProvider) {
        this.applicationContextProvider = null;
    }

    protected void bindActionsType(ServiceReference<ActionsType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ActionsType actionsType = context.getService(serviceReference);
        registerActions(actionsType, context);
    }

    protected void unbindActionsType(ActionsType actionsType) {
        // nothing to do
    }

    protected void activate(ComponentContext context) {
        contextInjector = new ContextInjector(activeContextProvider, applicationContextProvider);
        resolveUnresolvedItems();
    }

    protected void deactivate(ComponentContext context) {
        actionDescriptors.forEach(this::closeActionDescriptor);
        actionDescriptors.clear();
    }

    protected boolean isInitialized() {
        return activeContextProvider != null && applicationContextProvider != null && contextInjector != null;
    }

    protected abstract void registerActions(ActionsType actionType, BundleContext context);

    protected void resolveUnresolvedItems() {
        resolveUnresolvedActions();
        resolveUnresolvedActionDescriptors();
    }

    private void resolveUnresolvedActions() {
        unresolvedActions.forEach((entry) -> registerActionType(entry.getEntry(), entry.getContext()));
    }

    private void resolveUnresolvedActionDescriptors() {
        unresolvedActionDescriptors.forEach((entry) -> registerActionDescriptor(entry.getEntry(), entry.getContext()));
    }

    protected void registerActionType(final A actionType, final BundleContext context) {
        if (isInitialized()) {
            try {
                D actionDescriptor = createActionDescriptor(actionType, context);
                getActionRegistry().registerActionDescriptor(actionDescriptor, context);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | RuntimeException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        } else {
            unresolvedActions.add(new UnresolvedEntry<>(actionType, context));
        }
    }

    protected abstract D createActionDescriptor(A actionType, BundleContext context) throws IllegalAccessException, ClassNotFoundException, InstantiationException;

    protected void closeActionDescriptor(D actionDescriptor) {
        try {
            closeActionListener(actionDescriptor.getListener());
            actionDescriptor.setListener(null);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private void closeActionListener(Object listener) throws Exception {
        if (listener instanceof AutoCloseable) {
            ((AutoCloseable) listener).close();
        }
    }

    protected abstract void registerActionDescriptor(D actionDescriptor, BundleContext context);

    /**
     * @return the actionRegistry
     */
    protected abstract ActionRegistry<D> getActionRegistry();

    protected void registerActionDescriptor(D actionDescriptor) {
        actionDescriptors.add(actionDescriptor);
    }

    protected void unregisterActionDescriptor(D actionDescriptor) {
        closeActionDescriptor(actionDescriptor);
        actionDescriptors.remove(actionDescriptor);
    }

    protected void registerUnresolvedActionDescriptor(D actionDescriptor, BundleContext context) {
        unresolvedActionDescriptors.add(new UnresolvedEntry<>(actionDescriptor, context));
    }

    /**
     * @return the contextInjector
     */
    protected ContextInjector getContextInjector() {
        return contextInjector;
    }
}
