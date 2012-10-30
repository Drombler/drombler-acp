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
package org.drombler.acp.core.action.spi;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 *
 * @author puce
 */
public class ActionRegistry {

//    private final Map<String, ActionDescriptor> actionDescriptors = new HashMap<>();
    public <T> T getAction(String actionId, Class<T> actionClass, BundleContext context) {
        try {
            Collection<ServiceReference<T>> serviceReferences = context.getServiceReferences(
                    actionClass, "(" + ActionDescriptor.ID_KEY + "=" + actionId + ")");
            if (!serviceReferences.isEmpty()) {
                return context.getService(serviceReferences.iterator().next());
            }
        } catch (InvalidSyntaxException ex) {
            Logger.getLogger(ActionRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public <T> void registerAction(String actionId, Class<T> actionClass, T action, BundleContext context) {
        Dictionary<String, String> properties = new Hashtable<>(1);
        properties.put(ActionDescriptor.ID_KEY, actionId);
        context.registerService(actionClass, action, properties);
    }
//    public ActionDescriptor putAction(String actionId, ActionDescriptor action) {
//        return actionDescriptors.put(actionId, action);
//    }

    public String getActionId(ServiceReference<?> reference) {
        return reference.getProperty(ActionDescriptor.ID_KEY).toString();
    }
}
