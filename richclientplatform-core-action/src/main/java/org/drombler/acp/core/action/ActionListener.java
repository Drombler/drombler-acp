/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action;

import java.beans.PropertyChangeListener;

/**
 *
 * @author puce
 */
//TODO extend EventListener?
//TODO E extend EventObject?
public interface ActionListener<E> {

    void onAction(E event);

    boolean isDisabled();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
