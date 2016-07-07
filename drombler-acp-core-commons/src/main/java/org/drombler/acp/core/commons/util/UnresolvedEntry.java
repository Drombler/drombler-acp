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

import org.osgi.framework.BundleContext;

/**
 *
 * @author puce
 */
public class UnresolvedEntry<T> {
    private final T entry;
    private final BundleContext context;

    public UnresolvedEntry(T entry, BundleContext context) {
        this.entry = entry;
        this.context = context;
    }

    /**
     * @return the entry
     */
    public T getEntry() {
        return entry;
    }

    /**
     * @return the context
     */
    public BundleContext getContext() {
        return context;
    }
    
}
