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

import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.action.jaxb.ActionType;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.drombler.commons.context.ContextInjector;
import org.drombler.commons.context.ContextManager;
import org.drombler.commons.context.Contexts;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
/*package-private*/ final class ActionDescriptorUtils {

    private ActionDescriptorUtils() {
    }

    public static <T> void configureActionDescriptor(ActionDescriptor<T> actionDescriptor, ActionType actionType, Bundle bundle,
            ContextManager contextManager, ContextInjector contextInjector) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        boolean bundleRequired = ResourceBundleUtils.isPrefixedResourceString(actionType.getDisplayName()) || ResourceBundleUtils.isPrefixedResourceString(actionType.getAccelerator());
        ResourceBundle resourceBundle = bundleRequired
                ? ResourceBundleUtils.getConditionalResourceBundle(actionDescriptor.getListenerType(), actionType.getResourceBundleBaseName())
                : null;
        actionDescriptor.setId(StringUtils.stripToNull(actionType.getId()));
        actionDescriptor.setDisplayName(ResourceBundleUtils.getResourceStringPrefixed(actionType.getDisplayName(), resourceBundle));
        actionDescriptor.setAccelerator(ResourceBundleUtils.getResourceStringPrefixed(actionType.getAccelerator(), resourceBundle));
        actionDescriptor.setIcon(StringUtils.stripToNull(actionType.getIcon()));
        configureActionDescriptorListener(actionDescriptor, actionDescriptor.getListenerType(), contextManager, contextInjector);
    }

    private static <T> void configureActionDescriptorListener(ActionDescriptor<T> actionDescriptor,
            Class<T> actionListenerClass, ContextManager contextManager, ContextInjector contextInjector) throws InstantiationException, IllegalAccessException {
        T actionListener = actionListenerClass.newInstance();
        Contexts.configureObject(actionListener, contextManager, contextInjector);
        actionDescriptor.setListener(actionListener);
    }

}
