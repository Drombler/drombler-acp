/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.processing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 *
 * @author puce
 */
public class ActionsMap {

//    private final Map<String, ActionDescriptor> actionDescriptors = new HashMap<>();
    public ActionDescriptor getAction(String actionId, BundleContext context) {
        try {
            Collection<ServiceReference<ActionDescriptor>> serviceReferences = context.getServiceReferences(
                    ActionDescriptor.class, "(" + ActionDescriptor.ID_KEY + "=" + actionId);
            if (!serviceReferences.isEmpty()) {
                return context.getService(serviceReferences.iterator().next());
            }
        } catch (InvalidSyntaxException ex) {
            Logger.getLogger(ActionsMap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
//    public ActionDescriptor putAction(String actionId, ActionDescriptor action) {
//        return actionDescriptors.put(actionId, action);
//    }
}
