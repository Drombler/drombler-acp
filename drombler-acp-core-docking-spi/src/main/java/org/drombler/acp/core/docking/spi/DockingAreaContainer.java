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

import java.util.Set;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockingAreaDescriptor;

/**
 *
 * @author puce
 * @param <D>
 * @param <E>
 */
public interface DockingAreaContainer<D, E extends DockableEntry<D>> {

    boolean addDockingArea(DockingAreaDescriptor dockingAreaDescriptor);

    boolean addDockable(E dockableEntry);

    void addDockingAreaContainerListener(DockingAreaContainerListener<D, E> listener);

    void removeDockingAreaContainerListener(DockingAreaContainerListener<D, E> listener);

    String getDefaultEditorAreaId();

    Set<E> getDockables();

}
