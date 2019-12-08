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
package org.drombler.acp.core.docking.spi;

import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.context.DockingAreaContainer;

/**
 * A {@link DockingAreaContainer} provider.<br>
 * <br>
 * This is a SPI interface and must be implemented by a GUI toolkit specific extension.
 *
 * @author puce
 * @param <D>
 * @param <DATA>
 * @param <E>
 */
public interface DockingAreaContainerProvider<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> {

    /**
     * Gets the {@link DockingAreaContainer}.
     *
     * @return the DockingAreaContainer
     */
    DockingAreaContainer<D, DATA, E> getDockingAreaContainer();
}
