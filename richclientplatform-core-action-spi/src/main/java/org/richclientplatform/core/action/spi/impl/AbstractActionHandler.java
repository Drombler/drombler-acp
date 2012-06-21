/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.ArrayList;
import java.util.List;
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
import org.richclientplatform.core.lib.util.context.ActiveContextProvider;
import org.richclientplatform.core.lib.util.context.ApplicationContextProvider;
import org.richclientplatform.core.lib.util.context.Context;

/**
 *
 * @author puce
 */
@References({
    @Reference(name = "activeContextProvider", referenceInterface = ActiveContextProvider.class),
    @Reference(name = "applicationContextProvider", referenceInterface = ApplicationContextProvider.class),
    @Reference(name = "actionsType", referenceInterface = ActionsType.class,
    cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
})
public abstract class AbstractActionHandler<A, D> {

    private final ActionRegistry actionRegistry = new ActionRegistry();
    private Context activeContext;
    private Context applicationContext;
    private final List<UnresolvedEntry<A>> unresolvedActions = new ArrayList<>();
    private final List<UnresolvedEntry<D>> unresolvedActionDescriptors = new ArrayList<>();

    protected void bindActiveContextProvider(ActiveContextProvider activeContextProvider) {
        activeContext = activeContextProvider.getActiveContext();
    }

    protected void unbindActiveContextProvider(ActiveContextProvider activeContextProvider) {
        activeContext = null;
    }

    protected void bindApplicationContextProvider(ApplicationContextProvider applicationContextProvider) {
        applicationContext = applicationContextProvider.getApplicationContext();
    }

    protected void unbindApplicationContextProvider(ApplicationContextProvider applicationContextProvider) {
        applicationContext = null;
    }

    protected void bindActionsType(ServiceReference<ActionsType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        ActionsType actionsType = context.getService(serviceReference);
        registerActions(actionsType, context);
    }

    protected void unbindActionsType(ActionsType actionsType) {
        // TODO
    }

    protected boolean isInitialized() {
        return getActiveContext() != null && getApplicationContext() != null;
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

    protected void registerActionType(A actionType, BundleContext context) {
        if (isInitialized()) {
            try {
                D actionDescriptor = createActionDescriptor(actionType, context);
                registerActionDescriptor(actionDescriptor, context);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(ActionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
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
     * @return the activeContext
     */
    protected Context getActiveContext() {
        return activeContext;
    }

    /**
     * @return the applicationContext
     */
    protected Context getApplicationContext() {
        return applicationContext;
    }
}
