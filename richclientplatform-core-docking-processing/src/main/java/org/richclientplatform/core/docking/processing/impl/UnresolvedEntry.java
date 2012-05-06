/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.processing.impl;

import org.osgi.framework.BundleContext;

/**
 *
 * @author puce
 */
// TODO: consider to remove code duplication: org.richclientplatform.core.action.spi.impl.UnresolvedEntry
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
