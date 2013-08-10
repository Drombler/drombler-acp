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
package org.drombler.acp.core.action.spi.impl;

/**
 *
 * @author puce
 */
public class MenuItemConfig<Action> {

    private static final int ICON_SIZE = 16;
    private final Action action;

    public MenuItemConfig(Action action) {
        this.action = action;
    }

    /**
     * @return the action
     */
    public Action getAction() {
        return action;
    }

    /**
     * @return the iconSize
     */
    public int getIconSize() {
        return ICON_SIZE;
    }
}
