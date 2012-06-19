/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.osgi.framework.Bundle;
import org.richclientplatform.core.action.jaxb.ToggleActionType;
import org.richclientplatform.core.lib.util.Context;

/**
 *
 * @author puce
 */
public class ToggleActionDescriptor extends ActionDescriptor { // TODO: extend CheckActionDescriptor or ActionDescriptor? ToggleActionListener extends CheckActionListener...

    public static ToggleActionDescriptor createToggleActionDescriptor(ToggleActionType actionType, Bundle bundle,
            Context activeContext, Context applicationContext)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ToggleActionDescriptor actionDescriptor = new ToggleActionDescriptor();
        ActionDescriptorUtils.configureActionDescriptor(actionDescriptor, actionType, bundle, activeContext,
                applicationContext);
        return actionDescriptor;
    }
}
