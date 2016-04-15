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
package org.drombler.acp.core.docking.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.docking.spi.DockablePreferencesManagerProvider;
import org.drombler.commons.docking.DockablePreferencesManager;
import org.drombler.commons.docking.SimpleDockablePreferencesManager;

/**
 *
 * @author puce
 */
@Component
@Service
public class DockablePreferencesManagerProviderImpl<D> implements DockablePreferencesManagerProvider<D> {

    private final DockablePreferencesManager<D> dockablePreferencesManager = new SimpleDockablePreferencesManager<>();

    @Override
    public DockablePreferencesManager<D> getDockablePreferencesManager() {
        return dockablePreferencesManager;
    }

}