/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.projectx.contactcenter.desktop.action.processing;

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

    private String id;
    private String displayName;
    private String acceleratorKey;
    private Class<?> listenerClass;

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

    /**
     * @return the listenerClass
     */
    public Class<?> getListenerClass() {
        return listenerClass;
    }

    /**
     * @param listenerClass the listenerClass to set
     */
    public void setListenerClass(Class<?> listenerClass) {
        this.listenerClass = listenerClass;
    }

    public static ActionDescriptor createActionDescriptor(ActionType actionType, Bundle bundle) throws ClassNotFoundException {

        ActionDescriptor actionDescriptor = new ActionDescriptor();
        Class<?> actionListenerClass = getClass(actionType.getListenerClass(), bundle);
        actionDescriptor.setId(StringUtils.stripToNull(actionType.getId()));
        actionDescriptor.setDisplayName(getResourceString(actionListenerClass, actionType.getDisplayName()));
        actionDescriptor.setAcceleratorKey(getResourceString(actionListenerClass, actionType.getAcceleratorKey()));
        actionDescriptor.setListenerClass(actionListenerClass);
        return actionDescriptor;
    }

    private static String getResourceString(Class<?> type, String resourceKey) {
        String strippedResourceKey = StringUtils.stripToNull(resourceKey);
        if (strippedResourceKey != null && strippedResourceKey.startsWith("#")) {
            strippedResourceKey = strippedResourceKey.substring(1);
            ResourceBundle rb = ResourceBundle.getBundle(type.getPackage().getName() + ".Bundle", Locale.getDefault(), type.getClassLoader());
//            if (rb.containsKey(resourceKey)) {
            return rb.getString(strippedResourceKey);
//            }
        }
        return resourceKey;
    }

    private static Class<?> getClass(String listenerClass, Bundle bundle) throws ClassNotFoundException {
        return bundle.loadClass(StringUtils.stripToNull(listenerClass));
    }
}
