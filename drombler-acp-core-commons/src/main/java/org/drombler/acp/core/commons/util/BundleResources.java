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
package org.drombler.acp.core.commons.util;

import java.util.ResourceBundle;
import org.drombler.commons.client.util.Resources;
import org.osgi.framework.Bundle;

/**
 * A utility class for {@link ResourceBundle}s in OSGi {@link Bundle}s.
 *
 * @author puce
 */
public class BundleResources {

    private BundleResources() {
    }

    /**
     * Gets a resource string from a {@link ResourceBundle} called
     * Bundle.properties (or a if the value starts with '%' the rest of the
     * value is interpreted as a property key and the value gets looked-up in
     * the Bundle.properties file (or a locale specific derivation of this
     * file), which has to be in the same package as the annotated action.
     *
     * @param aPackage
     * @param resourceKey
     * @param bundle
     * @return
     */
    public static String getResourceString(String aPackage, String resourceKey, Bundle bundle) {
        return Resources.getResourceString(aPackage, resourceKey, new BundleProxyClassLoader(bundle));
    }
}
