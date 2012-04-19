/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action;

/**
 *
 * @author puce
 */
public interface CheckActionListener<E> extends ActionListener<E> {

    boolean isSelected();

    void onSelectionChanged(boolean oldValue, boolean newValue);
}
