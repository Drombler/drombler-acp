/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import org.osgi.framework.Bundle;
import org.richclientplatform.core.action.jaxb.CheckActionType;

/**
 *
 * @author puce
 */
public class CheckActionDescriptor extends ActionDescriptor {

    public static CheckActionDescriptor createCheckActionDescriptor(CheckActionType actionType, Bundle bundle)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        CheckActionDescriptor actionDescriptor = new CheckActionDescriptor();
        ActionDescriptorUtils.configureActionDescriptor(actionDescriptor, actionType, bundle);
        return actionDescriptor;
    }
}
