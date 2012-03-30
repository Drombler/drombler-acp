/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.processing;

import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
public class Resources {

    private Resources() {
    }

    public static String getResourceString(Class<?> type, String resourceKey) {
        return getResourceString(type.getPackage().getName(), resourceKey, type.getClassLoader());
    }

    public static ResourceBundle getResourceBundle(String aPackage, ClassLoader classLoader) {
        return ResourceBundle.getBundle(aPackage + ".Bundle", Locale.getDefault(), classLoader);
    }

    public static String getResourceString(String aPackage, String resourceKey, Bundle bundle) {
        return getResourceString(aPackage, resourceKey, new BundleProxyClassLoader(bundle));
    }

    private static String getResourceString(String aPackage, String resourceKey, ClassLoader classLoader) {
        String strippedResourceKey = StringUtils.stripToNull(resourceKey);
        if (strippedResourceKey != null && strippedResourceKey.startsWith("#")) {
            strippedResourceKey = strippedResourceKey.substring(1);
            ResourceBundle rb = getResourceBundle(aPackage, classLoader);
//            if (rb.containsKey(resourceKey)) {
            return rb.getString(strippedResourceKey);
//            }
        }
        return resourceKey;
    }
}
