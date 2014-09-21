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
import org.softsmithy.lib.util.ResourceLoader;

/**
 *
 * @author puce
 */
class ActionDescriptorUtils {

    private ActionDescriptorUtils() {
    }

    public static void configureActionDescriptor(ActionDescriptor actionDescriptor, ActionType actionType, Bundle bundle,
            ContextInjector contextInjector) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        Class<?> actionListenerClass = bundle.loadClass(StringUtils.stripToNull(actionType.getListenerClass()));
        actionDescriptor.setId(StringUtils.stripToNull(actionType.getId()));
        actionDescriptor.setDisplayName(ResourceBundleUtils.getPackageResourceStringPrefixed(actionListenerClass,
                actionType.getDisplayName()));
        actionDescriptor.setAccelerator(ResourceBundleUtils.getPackageResourceStringPrefixed(actionListenerClass,
                actionType.getAccelerator()));
        actionDescriptor.setIcon(StringUtils.stripToNull(actionType.getIcon()));
        actionDescriptor.setResourceLoader(new ResourceLoader(actionListenerClass));
        Object actionListener = actionListenerClass.newInstance();
        contextInjector.inject(actionListener);
        actionDescriptor.setListener(actionListener);
    }
}
