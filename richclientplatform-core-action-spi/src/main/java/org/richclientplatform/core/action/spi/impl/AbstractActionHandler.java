/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.richclientplatform.core.action.jaxb.ActionType;
import org.richclientplatform.core.action.jaxb.ActionsType;

/**
 *
 * @author puce
 */
@Reference(name = "actionsType", referenceInterface = ActionsType.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public abstract class AbstractActionHandler {

    public AbstractActionHandler() {
    }

    protected void bindActionsType(ServiceReference<ActionsType> serviceReference) {
        Bundle bundle = serviceReference.getBundle();
        BundleContext context = bundle.getBundleContext();
        ActionsType actionsType = context.getService(serviceReference);
       
    }

    protected void unbindActionsType(ActionsType actionsType) {
        // TODO
    }

    protected abstract void registerAction(ActionsType actionType, Bundle bundle, BundleContext context);
}
