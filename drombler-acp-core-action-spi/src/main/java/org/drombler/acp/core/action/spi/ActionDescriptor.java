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
 * An action descriptor.
 *
 * @author puce
 * @param <T> the listener type
 */
public class ActionDescriptor<T> {

    /**
     * The action id key used register and lookup actions by id.
     */
    public static String ID_KEY = "id";

    private final Class<T> listenerType;

    private String id;
    private String displayName;
    private String accelerator;
    private String icon;
    private T listener;
    private final ResourceLoader resourceLoader;

    /**
     * Creates a new instance of this class.
     *
     * @param listenerType the action listener type
     */
    public ActionDescriptor(Class<T> listenerType) {
        this(listenerType, new ResourceLoader(listenerType));
    }

    /**
     * Creates a new instance of this class.
     *
     * @param listenerType the action listener type
     * @param resourceLoader the resource loader
     */
    public ActionDescriptor(Class<T> listenerType, ResourceLoader resourceLoader) {
        this.listenerType = listenerType;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Gets the action id.
     *
     * @return the action id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the action id.
     *
     * @param id the action id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name.
     *
     * @param displayName the display name to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the accelerator.
     *
     * @return the accelerator
     */
    public String getAccelerator() {
        return accelerator;
    }

    /**
     * Sets the accelerator.
     *
     * @param accelerator the accelerator to set
     */
    public void setAccelerator(String accelerator) {
        this.accelerator = accelerator;
    }

    /**
     * Gets the icon name pattern to resolve the icons to be used for this action.
     *
     * Note that this property only specifies the name pattern. Drombler ACP looks for &lt;icon-base-name&gt;16.&lt;icon-extension&gt; for menu items (expected to be 16x16 pixels) and
     * &lt;icon-base-name&gt;24.&lt;icon-extension&gt; for toolbar buttons (expected to be 24x24 pixels). So if icon is &quot;test.png&quot;, Drombler ACP would look for test16.png (for menu items)
     * and test24.png (for toolbar buttons).
     *
     * @return the icon name pattern
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon name pattern to resolve the icons to be used for this action.
     *
     * Note that this property only specifies the name pattern. Drombler ACP looks for &lt;icon-base-name&gt;16.&lt;icon-extension&gt; for menu items (expected to be 16x16 pixels) and
     * &lt;icon-base-name&gt;24.&lt;icon-extension&gt; for toolbar buttons (expected to be 24x24 pixels). So if icon is &quot;test.png&quot;, Drombler ACP would look for test16.png (for menu items)
     * and test24.png (for toolbar buttons).
     *
     * @param icon the icon name pattern
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Gets the action listener.
     *
     * @return the action listener
     */
    public T getListener() {
        return listener;
    }

    /**
     * Sets the action listener.
     *
     * @param listener the action listener to set
     */
    public void setListener(T listener) {
        this.listener = listener;
    }

    /**
     * Gets the action listener type
     *
     * @return the action listener type
     */
    public Class<T> getListenerType() {
        return listenerType;
    }

    /**
     * Gets the resource loader to load resources such as icons.
     *
     * @return the resource loader
     */
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    /**
     * Creates an instance of an {@link ActionDescriptor} from an {@link ActionType} unmarshalled from the application.xml.
     *
     * @param actionType the unmarshalled ActionType
     * @param bundle the OSGi bundle
     * @param contextManager the context manager
     * @param contextInjector the context injector
     * @return a ActionDescriptor
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
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

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString() {
        return "ActionDescriptor[" + "id=" + id + ", displayName=" + displayName + ", listenerType=" + listenerType + ", accelerator=" + accelerator + ", icon=" + icon + ']';
    }

}
