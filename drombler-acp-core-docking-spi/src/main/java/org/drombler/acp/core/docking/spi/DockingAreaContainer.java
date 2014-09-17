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

import org.drombler.commons.client.docking.DockableData;
import org.drombler.commons.client.docking.DockableEntry;
import org.drombler.commons.client.docking.DockingAreaDescriptor;

/**
 *
 * @author puce
 * @param <D>
 * @param <DATA>
 * @param <E>
 */
public interface DockingAreaContainer<D, DATA extends DockableData, E extends DockableEntry<D, DATA>> {

    boolean addDockingArea(DockingAreaDescriptor dockingAreaDescriptor);

    boolean addDockable(E dockableEntry);

    void addDockingAreaContainerListener(DockingAreaContainerListener<E> listener);

    void removeDockingAreaContainerListener(DockingAreaContainerListener<E> listener);

}
