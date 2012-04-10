/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.processing;

import org.richclientplatform.core.util.Positionable;

/**
 *
 * @author puce
 */
public class PositionableMenuItemAdapter<T> implements Positionable {

    public static <S> PositionableMenuItemAdapter<S> wrapSeparator(S separatorMenuItem, int position) {
        return new PositionableMenuItemAdapter<>(separatorMenuItem, position, true);
    }

    public static <T> PositionableMenuItemAdapter<T> wrapMenuItem(T menuItem, int position) {
        return new PositionableMenuItemAdapter<>(menuItem, position, false);
    }
    private final T menuItem;
    private final int position;
    private final boolean separator;

    private PositionableMenuItemAdapter(T menuItem, int position, boolean separator) {
        this.menuItem = menuItem;
        this.position = position;
        this.separator = separator;
    }

    public T getMenuItem() {
        return menuItem;
    }

    @Override
    public int getPosition() {
        return position;
    }

    public boolean isSeparator() {
        return separator;
    }
}
