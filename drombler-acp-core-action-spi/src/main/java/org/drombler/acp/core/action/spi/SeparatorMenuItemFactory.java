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

/**
 * A separator menu item factory to create a GUI toolkit specific separator menu item component.<br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @author puce
 * @param <S> the GUI toolkit specific type for separator menu items
 */
public interface SeparatorMenuItemFactory<S> {

    /**
     * Creates a GUI toolkit specific separator menu item component.
     *
     * @return a GUI toolkit specific separator menu item component
     */
    S createSeparatorMenuItem();
}
