/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi;

import org.drombler.acp.core.lib.util.PositionableAdapter;

/**
 *
 * @author puce
 */
public class PositionableMenuItemAdapter<T> extends PositionableAdapter<T> {

    public static <S> PositionableMenuItemAdapter<S> wrapSeparator(S separatorMenuItem, int position) {
        return new PositionableMenuItemAdapter<>(separatorMenuItem, position, true);
    }

    public static <T> PositionableMenuItemAdapter<T> wrapMenuItem(T menuItem, int position) {
        return new PositionableMenuItemAdapter<>(menuItem, position, false);
    }
    private final boolean separator;

    private PositionableMenuItemAdapter(T menuItem, int position, boolean separator) {
        super(menuItem, position);
        this.separator = separator;
    }

    public boolean isSeparator() {
        return separator;
    }
}
