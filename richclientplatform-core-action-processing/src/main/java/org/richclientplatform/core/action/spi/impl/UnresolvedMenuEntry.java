/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.spi.impl;

import org.osgi.framework.BundleContext;
import org.richclientplatform.core.action.spi.MenuEntryDescriptor;

/**
 *
 * @author puce
 */
public class UnresolvedMenuEntry {
    private final MenuEntryDescriptor menuEntryDescriptor;
    private final BundleContext context;

    public UnresolvedMenuEntry(MenuEntryDescriptor menuEntryDescriptor, BundleContext context) {
        this.menuEntryDescriptor = menuEntryDescriptor;
        this.context = context;
    }

    /**
     * @return the menuEntryDescriptor
     */
    public MenuEntryDescriptor getMenuEntryDescriptor() {
        return menuEntryDescriptor;
    }

    /**
     * @return the context
     */
    public BundleContext getContext() {
        return context;
    }
    
}
