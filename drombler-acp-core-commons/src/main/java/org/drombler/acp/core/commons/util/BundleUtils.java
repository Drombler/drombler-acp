package org.drombler.acp.core.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * A utility class for OSGi {@link Bundle}s.
 *
 * @author puce
 */
public final class BundleUtils {

    private BundleUtils() {
    }


    /**
     * Loads a class by name from an OSGi bundle.
     *
     * @param bundle the OSGi bundle
     * @param className the class name of the class to load
     * @return the loaded class
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(Bundle bundle, String className) throws ClassNotFoundException {
        return bundle.loadClass(StringUtils.stripToNull(className));
    }

    /**
     * Gets the {@link ClassLoader} of an OSGi bundle.
     *
     * @param bundle the OSGi bundle
     * @return the ClassLoader of an OSGi bundle
     */
    public static ClassLoader getClassLoader(Bundle bundle) {
        return bundle.adapt(BundleWiring.class).getClassLoader();
    }
}
