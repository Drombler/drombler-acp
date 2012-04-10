/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.richclientplatform.core.action.jaxb.ActionType;
import org.richclientplatform.core.util.Resources;

/**
 *
 * @author puce
 */
public class ActionDescriptor {

    public static String ID_KEY = "id";
    private String id;
    private String displayName;
    private String accelerator;
    private String icon;
    private Object listener;

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

    public Object getListener() {
        return listener;
    }

    /**
     * @param listener the listener to set
     */
    public void setListener(Object listener) {
        this.listener = listener;
    }

    public static ActionDescriptor createActionDescriptor(ActionType actionType, Bundle bundle) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        ActionDescriptor actionDescriptor = new ActionDescriptor();
        Class<?> actionListenerClass = bundle.loadClass(StringUtils.stripToNull(actionType.getListenerClass()));
        actionDescriptor.setId(StringUtils.stripToNull(actionType.getId()));
        actionDescriptor.setDisplayName(Resources.getResourceString(actionListenerClass, actionType.getDisplayName()));
        actionDescriptor.setAccelerator(Resources.getResourceString(actionListenerClass, actionType.getAccelerator()));
        actionDescriptor.setIcon(StringUtils.stripToNull(actionType.getIcon()));
        actionDescriptor.setListener(actionListenerClass.newInstance());
        return actionDescriptor;
    }


}
