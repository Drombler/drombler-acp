/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.richclientplatform.core.action.jaxb.ActionType;
import org.richclientplatform.core.lib.util.Resources;

/**
 *
 * @author puce
 */
class ActionDescriptorUtils {

    private ActionDescriptorUtils() {
    }

    public static void configureActionDescriptor(ActionDescriptor actionDescriptor, ActionType actionType, Bundle bundle) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> actionListenerClass = bundle.loadClass(StringUtils.stripToNull(actionType.getListenerClass()));
        actionDescriptor.setId(StringUtils.stripToNull(actionType.getId()));
        actionDescriptor.setDisplayName(Resources.getResourceString(actionListenerClass, actionType.getDisplayName()));
        actionDescriptor.setAccelerator(Resources.getResourceString(actionListenerClass, actionType.getAccelerator()));
        actionDescriptor.setIcon(StringUtils.stripToNull(actionType.getIcon()));
        actionDescriptor.setListener(actionListenerClass.newInstance());
    }
}
