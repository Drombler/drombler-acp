/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author puce
 */
public abstract class AbstractActionListener<E> implements ActionListener<E> {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private boolean disabled = false;

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    protected void setDisabled(boolean disabled) {
        if (this.disabled != disabled) {
            boolean oldValue = this.disabled;
            this.disabled = disabled;
            propertyChangeSupport.firePropertyChange("disabled", oldValue, disabled);
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
}
