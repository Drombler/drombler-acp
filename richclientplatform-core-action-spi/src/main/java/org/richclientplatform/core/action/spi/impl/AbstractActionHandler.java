/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.action.jaxb.ActionsType;
import org.richclientplatform.core.action.spi.ActionRegistry;
import org.richclientplatform.core.application.ApplicationExecutorProvider;
import org.richclientplatform.core.lib.util.context.ActiveContextProvider;
import org.richclientplatform.core.lib.util.context.ApplicationContextProvider;

/**
 *
 * @author puce
 */
@References({
    @Reference(name = "actionsType", referenceInterface = ActionsType.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "applicationExecutorProvider", referenceInterface = ApplicationExecutorProvider.class)
})
public abstract class AbstractActionHandler<A, D> {

    @Reference
    private ActiveContextProvider activeContextProvider;
    @Reference
    private ApplicationContextProvider applicationContextProvider;
    private Executor applicationExecutor;
    private final ActionRegistry actionRegistry = new ActionRegistry();
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
        // TODO
    }

    protected void bindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = applicationExecutorProvider.getApplicationExecutor();
    }

    protected void unbindApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        applicationExecutor = null;
    }

    protected boolean isInitialized() {
        return getActiveContextProvider() != null && getApplicationContextProvider() != null && applicationExecutor != null;
    }

    protected abstract void registerActions(ActionsType actionType, BundleContext context);

    protected void resolveUnresolvedItems() {
        resolveUnresolvedActions();
        resolveUnresolvedActionDescriptors();
    }

    private void resolveUnresolvedActions() {
        for (UnresolvedEntry<A> entry : unresolvedActions) {
            registerActionType(entry.getEntry(), entry.getContext());
        }
    }

    private void resolveUnresolvedActionDescriptors() {
        for (UnresolvedEntry<D> entry : unresolvedActionDescriptors) {
            registerActionDescriptor(entry.getEntry(), entry.getContext());
        }
    }

    protected void registerActionType(final A actionType, final BundleContext context) {
        if (isInitialized()) {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    try {
                        D actionDescriptor = createActionDescriptor(actionType, context);
                        registerActionDescriptor(actionDescriptor, context);
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                        Logger.getLogger(ActionHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            applicationExecutor.execute(runnable);
        } else {
            unresolvedActions.add(new UnresolvedEntry<>(actionType, context));
        }
    }

    protected abstract D createActionDescriptor(A actionType, BundleContext context)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException;

    protected abstract void registerActionDescriptor(D actionDescriptor, BundleContext context);

    protected void registerUnresolvedActionDescriptor(D actionDescriptor, BundleContext context) {
        unresolvedActionDescriptors.add(new UnresolvedEntry<>(actionDescriptor, context));
    }

    /**
     * @return the actionRegistry
     */
    protected ActionRegistry getActionRegistry() {
        return actionRegistry;
    }

    /**
     * @return the activeContextProvider
     */
    protected ActiveContextProvider getActiveContextProvider() {
        return activeContextProvider;
    }

    /**
     * @return the applicationContextProvider
     */
    protected ApplicationContextProvider getApplicationContextProvider() {
        return applicationContextProvider;
    }
}
