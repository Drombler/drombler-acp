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
import java.util.regex.Matcher;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 * @param <D> the ActionDescriptor type
 */
public class ActionRegistry<D extends ActionDescriptor<?>> {

    private static final Logger LOG = LoggerFactory.getLogger(ActionRegistry.class);
    private static final String BACKSLASH = "\\";
    private static final String NUL = Character.toString((char) 0);
    private final Class<D> actionDescriptorType;

    public ActionRegistry(Class<D> actionDescriptorType) {
        this.actionDescriptorType = actionDescriptorType;
    }

//    private final Map<String, ActionDescriptor> actionDescriptors = new HashMap<>();
    public <T> T getAction(String actionId, Class<T> actionClass, BundleContext context) {
        try {
            Collection<ServiceReference<T>> serviceReferences = context.getServiceReferences(actionClass, createActionFilter(actionId));
            if (!serviceReferences.isEmpty()) {
                return context.getService(serviceReferences.iterator().next());
            }
        } catch (InvalidSyntaxException ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    private String escapeLDAPFilter(String actionId) {
        if (actionId.contains(BACKSLASH)) {
            actionId = actionId.replaceAll(Matcher.quoteReplacement(BACKSLASH), "\\5C");
        }
        if (actionId.contains(NUL)) {
            actionId = actionId.replaceAll(NUL, "\\00");
       }
        if (actionId.contains("(")) {
            actionId = actionId.replaceAll("\\(", "\\28");
        }
        if (actionId.contains(")")) {
            actionId = actionId.replaceAll("\\)", "\\29");
        }
        if (actionId.contains("*")) {
            actionId = actionId.replaceAll("\\*", "\\5C");
        }
        return actionId;
    }

    public <T> void registerAction(String actionId, Class<T> actionClass, T action, BundleContext context) {
        Dictionary<String, String> properties = new Hashtable<>(1);
        properties.put(ActionDescriptor.ID_KEY, escapeLDAPFilter(actionId));
        context.registerService(actionClass, action, properties);
    }
//    public ActionDescriptor putAction(String actionId, ActionDescriptor action) {
//        return actionDescriptors.put(actionId, action);
//    }

    public String getActionId(ServiceReference<?> reference) {
        return reference.getProperty(ActionDescriptor.ID_KEY).toString();
    }

    public void registerActionDescriptor(D actionDescriptor, BundleContext context) {
        Dictionary<String, String> properties = new Hashtable<>(1);
        properties.put(ActionDescriptor.ID_KEY, escapeLDAPFilter(actionDescriptor.getId()));
        context.registerService(actionDescriptorType, actionDescriptor, properties);
    }

    public D getActionDescriptor(String actionId, BundleContext context) {
        try {
            Collection<ServiceReference<D>> serviceReferences = context.getServiceReferences(actionDescriptorType, createActionFilter(actionId));
            if (!serviceReferences.isEmpty()) {
                return context.getService(serviceReferences.iterator().next());
            }
        } catch (InvalidSyntaxException ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    private String createActionFilter(String actionId) {
        return "(" + ActionDescriptor.ID_KEY + "=" + escapeLDAPFilter(actionId) + ")";
    }
}
