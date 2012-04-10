/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action;

import java.beans.PropertyChangeListener;

/**
 *
 * @author puce
 */
//TODO extend EventListener?
//TODO E extend EventObject?
public interface ActionListener<E> {

    void actionPerformed(E event);

    boolean isDisabled();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
