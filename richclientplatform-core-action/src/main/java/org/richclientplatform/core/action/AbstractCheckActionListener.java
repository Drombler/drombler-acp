/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action;

/**
 *
 * @author puce
 */
public abstract class AbstractCheckActionListener<E> extends AbstractActionListener<E> implements CheckActionListener<E> {

    private boolean selected = false;

    @Override
    public boolean isSelected() {
        return selected;
    }

    protected void setSelected(boolean selected) {
        if (this.selected != selected) {
            boolean oldValue = this.selected;
            this.selected = selected;
            getPropertyChangeSupport().firePropertyChange("selected", oldValue, selected);
        }
    }

    @Override
    public void onAction(E event) {
        // do nothing
    }

}
