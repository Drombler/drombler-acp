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
import org.drombler.commons.context.ContextInjector;
import org.drombler.commons.context.ContextManager;
import org.osgi.framework.Bundle;
import org.softsmithy.lib.util.ResourceLoader;

/**
 *
 * @author puce
 * @param <T> the listener type
 */
public class ActionDescriptor<T> {

    public static String ID_KEY = "id";

    private final Class<T> listenerType;

    private String id;
    private String displayName;
    private String accelerator;
    private String icon;
    private T listener;
    private final ResourceLoader resourceLoader;

    public ActionDescriptor(Class<T> listenerType) {
        this(listenerType, new ResourceLoader(listenerType));
    }

    public ActionDescriptor(Class<T> listenerType, ResourceLoader resourceLoader) {
        this.listenerType = listenerType;
        this.resourceLoader = resourceLoader;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the acceleratorKey
     */
    public String getAccelerator() {
        return accelerator;
    }

    /**
     * @param acceleratorKey the acceleratorKey to set
     */
    public void setAccelerator(String acceleratorKey) {
        this.accelerator = acceleratorKey;
    }

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public T getListener() {
        return listener;
    }

    /**
     * @param listener the listener to set
     */
    public void setListener(T listener) {
        this.listener = listener;
    }

    /**
     * @return the listenerType
     */
    public Class<T> getListenerType() {
        return listenerType;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public static ActionDescriptor<?> createActionDescriptor(ActionType actionType, Bundle bundle,
            ContextManager contextManager, ContextInjector contextInjector)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> actionListenerClass = bundle.loadClass(StringUtils.stripToNull(actionType.getListenerClass()));
        return createActionDescriptor(actionListenerClass, actionType, bundle, contextManager, contextInjector);
    }

    private static <T> ActionDescriptor<T> createActionDescriptor(Class<T> actionListenerClass, ActionType actionType,
            Bundle bundle, ContextManager contextManager, ContextInjector contextInjector)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ActionDescriptor<T> actionDescriptor = new ActionDescriptor<>(actionListenerClass);
        ActionDescriptorUtils.configureActionDescriptor(actionDescriptor, actionType, bundle, contextManager, contextInjector);
        return actionDescriptor;
    }
}
