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
 * Copyright 2014 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.startup.main;

/**
 * The Main Window provider.<br>
 * <br>
 * The returned Main Window should only be used for window positioning purposes and as a parent of dialogs.<br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @param <T> the GUI toolkit specific type of the Main Window
 * @author puce
 */
@FunctionalInterface
public interface MainWindowProvider<T> {

    /**
     * Gets the Main Window. It should only be used for window positioning purposes and as a parent of dialogs.
     *
     * @return the Main Window
     */
    T getMainWindow();

}
