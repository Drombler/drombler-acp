package org.drombler.acp.core.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * Utility class for {@link Bundle}.
 *
 * @author puce
 */
public final class BundleUtils {

    /**
     * Loads a class from a {@link Bundle}.
     *
     * @param bundle the bundle to load the class from
     * @param className the fully qualified class name
     * @return the class object
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(Bundle bundle, String className) throws ClassNotFoundException {
        return bundle.loadClass(StringUtils.stripToNull(className));
    }

    /**
     * Gets the {@link ClassLoader} of the specified {@link Bundle}.
     *
     * @param bundle the bundle
     * @return the class loader of the bundle
     */
    public static ClassLoader getClassLoader(Bundle bundle) {
        return bundle.adapt(BundleWiring.class).getClassLoader();
    }
}
