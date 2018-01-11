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
import org.drombler.acp.core.action.jaxb.ActionsType;
import org.drombler.acp.core.action.spi.ActionDescriptor;
import org.drombler.acp.core.action.spi.ActionRegistry;
import org.drombler.acp.core.commons.util.UnresolvedEntry;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.acp.core.context.ContextManagerProvider;
import org.drombler.commons.context.ContextInjector;
import org.drombler.commons.context.ContextManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
public abstract class AbstractActionHandler<A, D extends ActionDescriptor<?>> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractActionHandler.class);

    @Reference
    protected ApplicationThreadExecutorProvider applicationThreadExecutorProvider;
    @Reference
    protected ContextManagerProvider contextManagerProvider;
    private ContextInjector contextInjector;

    private final List<D> actionDescriptors = new ArrayList<>();
    private final List<UnresolvedEntry<A>> unresolvedActions = new ArrayList<>();
    private final List<UnresolvedEntry<D>> unresolvedActionDescriptors = new ArrayList<>();

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindActionsType(ServiceReference<ActionsType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ActionsType actionsType = context.getService(serviceReference);
        registerActions(actionsType, context);
    }

    protected void unbindActionsType(ActionsType actionsType) {
        // nothing to do
    }

    protected void activate(ComponentContext context) {
        contextInjector = new ContextInjector(getContextManager());
        resolveUnresolvedItems();
    }

    protected void deactivate(ComponentContext context) {
        actionDescriptors.forEach(this::closeActionDescriptor);
        actionDescriptors.clear();
    }

    protected boolean isInitialized() {
        return contextManagerProvider != null && contextInjector != null && applicationThreadExecutorProvider != null;
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
            registerActionTypeInitialized(actionType, context);
        } else {
            unresolvedActions.add(new UnresolvedEntry<>(actionType, context));
        }
    }

    private void registerActionTypeInitialized(final A actionType, final BundleContext context) {
        applicationThreadExecutorProvider.getApplicationThreadExecutor().execute(() -> { // register local contexts on application thread
            try {
                D actionDescriptor = createActionDescriptor(actionType, context, getContextManager(), contextInjector);
                getActionRegistry().registerActionDescriptor(actionDescriptor, context);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | RuntimeException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }

    protected abstract D createActionDescriptor(A actionType, BundleContext context, ContextManager contextManager, ContextInjector contextInjector)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException;

    protected void closeActionDescriptor(D actionDescriptor) {
        try {
            closeActionListener(actionDescriptor.getListener());
            actionDescriptor.setListener(null);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private void closeActionListener(Object listener) throws Exception {
        getContextManager().removeLocalContext(listener);
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

    private ContextManager getContextManager() {
        return contextManagerProvider.getContextManager();
    }
}
