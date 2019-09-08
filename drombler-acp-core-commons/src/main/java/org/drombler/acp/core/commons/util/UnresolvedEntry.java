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

import java.util.Objects;
import org.osgi.framework.BundleContext;

/**
 * An unresolved entry which keeps a reference to its {@link BundleContext}.
 *
 * @author puce
 * @param <T> the type of the entry
 */
public class UnresolvedEntry<T> {
    private final T entry;
    private final BundleContext context;

    /**
     * Creates a new instance of this class.
     *
     * @param entry the unresolved entry
     * @param context the bundle context of the unresolved entry
     */
    public UnresolvedEntry(T entry, BundleContext context) {
        this.entry = entry;
        this.context = context;
    }

    /**
     * Gets the unresolved entry.
     *
     * @return the unresolved entry
     */
    public T getEntry() {
        return entry;
    }

    /**
     * Gets the {@link BundleContext} of the unresolved entry.
     *
     * @return the bundle context of the unresolved entry
     */
    public BundleContext getContext() {
        return context;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.entry);
        hash = 59 * hash + Objects.hashCode(this.context);
        return hash;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UnresolvedEntry)) {
            return false;
        }

        UnresolvedEntry<?> other = (UnresolvedEntry<?>) obj;
        return Objects.equals(this.entry, other.entry)
                && Objects.equals(this.context, other.context);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString() {
        return "UnresolvedEntry[" + "entry=" + entry + ']';
    }

}
