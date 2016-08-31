package org.drombler.acp.core.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
public class BundleUtils {

    public static Class<?> loadClass(Bundle bundle, String className) throws ClassNotFoundException {
        return bundle.loadClass(StringUtils.stripToNull(className));
    }
}
