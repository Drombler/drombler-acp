/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2012 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.action.spi;

import org.drombler.acp.core.commons.util.PositionableAdapter;

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
