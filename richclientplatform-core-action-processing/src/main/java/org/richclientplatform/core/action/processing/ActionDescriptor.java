/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.processing;

import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.richclientplatform.core.action.jaxb.ActionType;

/**
 *
 * @author puce
 */
public class ActionDescriptor {
    public static String ID_KEY = "id";

    private String id;
    private String displayName;
    private String acceleratorKey;
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
    public String getAcceleratorKey() {
        return acceleratorKey;
    }

    /**
     * @param acceleratorKey the acceleratorKey to set
     */
    public void setAcceleratorKey(String acceleratorKey) {
        this.acceleratorKey = acceleratorKey;
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
        Class<?> actionListenerClass = getClass(actionType.getListenerClass(), bundle);
        actionDescriptor.setId(StringUtils.stripToNull(actionType.getId()));
        actionDescriptor.setDisplayName(Resources.getResourceString(actionListenerClass, actionType.getDisplayName()));
        actionDescriptor.setAcceleratorKey(Resources.getResourceString(actionListenerClass, actionType.getAcceleratorKey()));
        actionDescriptor.setListener(actionListenerClass.newInstance());
        return actionDescriptor;
    }



    private static Class<?> getClass(String listenerClass, Bundle bundle) throws ClassNotFoundException {
        return bundle.loadClass(StringUtils.stripToNull(listenerClass));
    }
}
