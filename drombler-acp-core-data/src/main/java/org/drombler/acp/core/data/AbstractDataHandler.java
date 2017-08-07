/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (GitHub user: puce77).
 * Copyright 2016 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.commons.context.Context;
import org.drombler.commons.context.SimpleContext;
import org.drombler.commons.context.SimpleContextContent;
import org.drombler.commons.data.CloseEvent;
import org.drombler.commons.data.CloseEventListener;
import org.drombler.commons.data.DataCapabilityProvider;
import org.drombler.commons.data.DataHandler;
import org.osgi.util.tracker.ServiceTracker;

/**
 * An abstract {@link DataHandler}. It observes registered {@link DataCapabilityProvider}s and adds the found data capabilities to it's local context.
 *
 * You can use this class as a base class for your own data handler implementations.
 *
 * @param <T> the type of the unique key of this data handler
 * @see AbstractDocumentHandler
 * @author puce
 */
public abstract class AbstractDataHandler<T> implements DataHandler<T> {

    private final SimpleContextContent contextContent = new SimpleContextContent();
    private final Context localContext = new SimpleContext(contextContent);
    private final ServiceTracker<DataCapabilityProvider, DataCapabilityProvider> dataCapabilityProviderTracker;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final List<CloseEventListener> closeEventListeners = new ArrayList<>();

    private boolean dirty = false;

    /**
     * Creates a new instance of this class.
     */
    public AbstractDataHandler() {
        this.dataCapabilityProviderTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DataCapabilityProvider.class, this::addDataCapabilityProvider, this::removeDataCapabilityProvider);
        dataCapabilityProviderTracker.open(true);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Context getLocalContext() {
        return localContext;
    }

    protected SimpleContextContent getContextContent() {
        return contextContent;
    }

    private void addDataCapabilityProvider(DataCapabilityProvider<?> dataCapabilityProvider) {
        contextContent.add(dataCapabilityProvider.getDataCapability(this));
    }

    private void removeDataCapabilityProvider(DataCapabilityProvider<?> dataCapabilityProvider) {
        // TODO: does this work? memory leak?
        contextContent.remove(dataCapabilityProvider.getDataCapability(this));
    }

    @Override
    public void markDirty() {
        setDirty(true);
    }

    protected void markClean() {
        setDirty(false);
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    private void setDirty(boolean dirty) {
        boolean oldDirty = this.dirty;
        this.dirty = dirty;
        propertyChangeSupport.firePropertyChange(DIRTY_PROPERTY_NAME, oldDirty, this.dirty);
    }

    protected PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void close() {
        dataCapabilityProviderTracker.close();
        fireCloseEvent();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addCloseEventListener(CloseEventListener listener) {
        closeEventListeners.add(listener);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void removeCloseEventListener(CloseEventListener listener) {
        closeEventListeners.remove(listener);
    }

    protected void fireCloseEvent() {
        CloseEvent event = new CloseEvent(this);
        closeEventListeners.forEach(listener -> listener.onClose(event));
    }
}
