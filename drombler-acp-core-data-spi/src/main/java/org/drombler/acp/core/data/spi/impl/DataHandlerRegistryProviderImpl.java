/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (GitHub user: puce77).
 * Copyright 2016 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.data.spi.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.drombler.commons.data.DataHandlerRegistry;
import org.osgi.service.component.ComponentContext;

/**
 *
 *
 * @author puce
 */
@Component
@Service
public class DataHandlerRegistryProviderImpl implements DataHandlerRegistryProvider {

    private DataHandlerRegistry dataHandlerRegistry;

    @Activate
    protected void activate(ComponentContext context) {
        dataHandlerRegistry = new DataHandlerRegistry();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        dataHandlerRegistry.close();
        dataHandlerRegistry = null;
    }

    @Override
    public DataHandlerRegistry getDataHandlerRegistry() {
        return dataHandlerRegistry;
    }
}
