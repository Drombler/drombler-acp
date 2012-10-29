/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi;

import org.osgi.framework.Bundle;
import org.drombler.acp.core.action.jaxb.ToggleActionType;
import org.drombler.acp.core.lib.util.context.Context;

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
