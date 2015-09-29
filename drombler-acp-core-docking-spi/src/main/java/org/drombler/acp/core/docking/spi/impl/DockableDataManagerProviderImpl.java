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
package org.drombler.acp.core.docking.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.docking.spi.DockableDataManagerProvider;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableDataManager;
import org.drombler.commons.docking.SimpleDockableDataManager;

/**
 *
 * @author puce
 */
@Component
@Service
public class DockableDataManagerProviderImpl<D, DATA extends DockableData> implements
        DockableDataManagerProvider<D, DockableData> {

    private final DockableDataManager<D, DockableData> dockableDataManager = new SimpleDockableDataManager<>();

    @Override
    public DockableDataManager<D, DockableData> getDockableDataManager() {
        return dockableDataManager;
    }

}
