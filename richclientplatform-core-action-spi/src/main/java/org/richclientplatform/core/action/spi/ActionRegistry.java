/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

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
}
