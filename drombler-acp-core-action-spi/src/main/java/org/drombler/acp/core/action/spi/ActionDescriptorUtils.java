/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.drombler.acp.core.action.jaxb.ActionType;
import org.drombler.acp.core.lib.util.context.ActiveContextSensitive;
import org.drombler.acp.core.lib.util.context.ApplicationContextSensitive;
import org.drombler.acp.core.lib.util.context.Context;
import org.drombler.acp.core.lib.util.Resources;

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
