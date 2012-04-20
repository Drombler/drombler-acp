/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action;

/**
 *
 * @author puce
 */
public abstract class AbstractToggleActionListener<E> extends AbstractActionListener<E> implements ToggleActionListener<E> {

    private boolean selected = false;

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            boolean oldValue = this.selected;
            this.selected = selected;
            onSelectionChanged(oldValue, selected);
            getPropertyChangeSupport().firePropertyChange("selected", oldValue, selected);
        }
    }

    @Override
    public void onAction(E event) {
        // do nothing
    }
}
