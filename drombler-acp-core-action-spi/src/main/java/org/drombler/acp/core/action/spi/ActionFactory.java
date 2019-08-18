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
 * An action factory. <br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @author puce
 * @param <T> the action type
 */
public interface ActionFactory<T> {

    /**
     * Creates a GUI toolkit specific action based on the provided action descriptor.
     *
     * @param actionDescriptor the action descriptor
     * @return a GUI toolkit specific action
     */
    T createAction(ActionDescriptor<?> actionDescriptor);

    /**
     * Gets the action class.
     *
     * @return the action class
     */
    Class<T> getActionClass();
}
