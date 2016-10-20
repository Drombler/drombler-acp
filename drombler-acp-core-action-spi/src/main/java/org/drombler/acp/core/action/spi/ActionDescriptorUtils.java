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

import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.action.jaxb.ActionType;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.drombler.commons.context.ContextInjector;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
class ActionDescriptorUtils {

    private ActionDescriptorUtils() {
    }

    public static <T> void configureActionDescriptor(ActionDescriptor<T> actionDescriptor, ActionType actionType,
            Bundle bundle, ContextInjector contextInjector) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        actionDescriptor.setId(StringUtils.stripToNull(actionType.getId()));
        actionDescriptor.setDisplayName(ResourceBundleUtils.getPackageResourceStringPrefixed(actionDescriptor.
                getListenerType(),
                actionType.getDisplayName()));
        actionDescriptor.setAccelerator(ResourceBundleUtils.getPackageResourceStringPrefixed(actionDescriptor.
                getListenerType(),
                actionType.getAccelerator()));
        actionDescriptor.setIcon(StringUtils.stripToNull(actionType.getIcon()));
        configureActionDescriptorListener(actionDescriptor, actionDescriptor.getListenerType(), contextInjector);

    }

    private static <T> void configureActionDescriptorListener(ActionDescriptor<T> actionDescriptor,
            Class<T> actionListenerClass, ContextInjector contextInjector) throws InstantiationException,
            IllegalAccessException {
        T actionListener = actionListenerClass.newInstance();
        contextInjector.inject(actionListener);
        actionDescriptor.setListener(actionListener);
    }
}
