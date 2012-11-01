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

import org.drombler.acp.core.action.jaxb.ToggleActionType;
import org.drombler.acp.core.commons.util.context.Context;
import org.osgi.framework.Bundle;

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
