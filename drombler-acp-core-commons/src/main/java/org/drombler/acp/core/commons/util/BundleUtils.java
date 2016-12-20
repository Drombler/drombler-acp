package org.drombler.acp.core.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 *
 * @author puce
 */
public class BundleUtils {

    public static Class<?> loadClass(Bundle bundle, String className) throws ClassNotFoundException {
        return bundle.loadClass(StringUtils.stripToNull(className));
    }

    public static ClassLoader getClassLoader(Bundle bundle) {
        return bundle.adapt(BundleWiring.class).getClassLoader();
    }
}
