/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.osgi.framework.Bundle;
import org.richclientplatform.core.action.jaxb.ToggleActionType;

/**
 *
 * @author puce
 */
public class ToggleActionDescriptor extends CheckActionDescriptor { // TODO: extend CheckActionDescriptor or ActionDescriptor? ToggleActionListener extends CheckActionListener...

    public static ToggleActionDescriptor createToggleActionDescriptor(ToggleActionType actionType, Bundle bundle)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ToggleActionDescriptor actionDescriptor = new ToggleActionDescriptor();
        ActionDescriptorUtils.configureActionDescriptor(actionDescriptor, actionType, bundle);
        return actionDescriptor;
    }
}
