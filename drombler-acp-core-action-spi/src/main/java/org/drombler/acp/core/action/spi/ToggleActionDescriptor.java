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
import org.drombler.acp.core.action.jaxb.ToggleActionType;
import org.drombler.commons.context.ContextInjector;
import org.drombler.commons.context.ContextManager;
import org.osgi.framework.Bundle;
import org.softsmithy.lib.util.ResourceLoader;

/**
 * A toggle action descriptor.
 *
 * @author puce
 * @param <T> the listener type
 */
public class ToggleActionDescriptor<T> extends ActionDescriptor<T> { // TODO: extend CheckActionDescriptor or ActionDescriptor? ToggleActionListener extends CheckActionListener...

    /**
     * Creates a new instance of this class.
     *
     * @param actionListenerClass
     */
    public ToggleActionDescriptor(Class<T> actionListenerClass) {
        super(actionListenerClass);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param listenerType the listener type
     * @param resourceLoader the resource loader
     */
    public ToggleActionDescriptor(Class<T> listenerType, ResourceLoader resourceLoader) {
        super(listenerType, resourceLoader);
    }

    /**
     * Creates an instance of a {@link ToggleActionDescriptor} from a {@link ToggleActionType} unmarshalled from the application.xml.
     *
     * @param actionType the unmarshalled ActionType
     * @param bundle the OSGi bundle
     * @param contextManager the context manager
     * @param contextInjector the context injector
     * @return a ToggleActionDescriptor
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static ToggleActionDescriptor<?> createToggleActionDescriptor(ToggleActionType actionType, Bundle bundle,
            ContextManager contextManager, ContextInjector contextInjector) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> actionListenerClass = bundle.loadClass(StringUtils.stripToNull(actionType.getListenerClass()));
        return createToggleActionDescriptor(actionListenerClass, actionType, bundle, contextManager, contextInjector);
    }

    private static <T> ToggleActionDescriptor<T> createToggleActionDescriptor(Class<T> actionListenerClass, ActionType actionType, Bundle bundle,
            ContextManager contextManager, ContextInjector contextInjector) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ToggleActionDescriptor<T> actionDescriptor = new ToggleActionDescriptor<>(actionListenerClass);
        ActionDescriptorUtils.configureActionDescriptor(actionDescriptor, actionType, bundle, contextManager, contextInjector);
        return actionDescriptor;
    }
}
