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
 * A tool bar factory to create a GUI toolkit specific tool bar.<br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @param <T> the GUI toolkit specific type for tool bars
 * @author puce
 */
public interface ToolBarFactory<T> {

    /**
     * Creates a GUI toolkit specific tool bar.
     *
     * @param toolBarDescriptor the tool bar descriptor
     * @return a new tool bar
     */
    T createToolBar(ToolBarDescriptor toolBarDescriptor);
}
