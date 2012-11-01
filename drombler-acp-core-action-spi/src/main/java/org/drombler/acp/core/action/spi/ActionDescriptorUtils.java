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

import org.apache.commons.lang.StringUtils;
import org.drombler.acp.core.action.jaxb.ActionType;
import org.drombler.acp.core.commons.util.Resources;
import org.drombler.acp.core.commons.util.context.ActiveContextSensitive;
import org.drombler.acp.core.commons.util.context.ApplicationContextSensitive;
import org.drombler.acp.core.commons.util.context.Context;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
class ActionDescriptorUtils {

    private ActionDescriptorUtils() {
    }

    public static void configureActionDescriptor(ActionDescriptor actionDescriptor, ActionType actionType, Bundle bundle, 
            Context activeContext, Context applicationContext) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> actionListenerClass = bundle.loadClass(StringUtils.stripToNull(actionType.getListenerClass()));
        actionDescriptor.setId(StringUtils.stripToNull(actionType.getId()));
        actionDescriptor.setDisplayName(Resources.getResourceString(actionListenerClass, actionType.getDisplayName()));
        actionDescriptor.setAccelerator(Resources.getResourceString(actionListenerClass, actionType.getAccelerator()));
        actionDescriptor.setIcon(StringUtils.stripToNull(actionType.getIcon()));
        Object actionListener = actionListenerClass.newInstance();
        if (actionListener instanceof ActiveContextSensitive) {
            ((ActiveContextSensitive) actionListener).setActiveContext(activeContext);
        }
        if (actionListener instanceof ApplicationContextSensitive) {
            ((ApplicationContextSensitive) actionListener).setApplicationContext(applicationContext);
        }
        actionDescriptor.setListener(actionListener);
    }
}
