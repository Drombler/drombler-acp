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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 * @deprecated bundle.adapt(BundleWiring.class).getClassLoader()
 */
@Deprecated
class BundleProxyClassLoader extends ClassLoader {

    private final Bundle bundle;
    private ClassLoader parent;

    public BundleProxyClassLoader(Bundle bundle) {
        this.bundle = bundle;
    }

    public BundleProxyClassLoader(Bundle bundle, ClassLoader parent) {
        super(parent);
        this.parent = parent;
        this.bundle = bundle;
    }
  
    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return bundle.getResources(name);
    }

    @Override
    public URL findResource(String name) {
        return bundle.getResource(name);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return bundle.loadClass(name);
    }

    @Override
    public URL getResource(String name) {
        return (parent == null) ? findResource(name) : super.getResource(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = (parent == null) ? findClass(name) : super.loadClass(name, false);
        if (resolve) {
            super.resolveClass(clazz);
        }

        return clazz;
    }
}
