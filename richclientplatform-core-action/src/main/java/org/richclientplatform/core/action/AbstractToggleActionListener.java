/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action;

/**
 *
 * @author puce
 */
public abstract class AbstractToggleActionListener<E> extends AbstractCheckActionListener<E> implements ToggleActionListener<E> {

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
    }
}
