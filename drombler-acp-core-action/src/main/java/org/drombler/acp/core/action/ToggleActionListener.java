/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action;

/**
 *
 * @author puce
 */
public interface ToggleActionListener<E> extends ActionListener<E> {

    boolean isSelected();

    void setSelected(boolean selected);

    void onSelectionChanged(boolean oldValue, boolean newValue);
}
