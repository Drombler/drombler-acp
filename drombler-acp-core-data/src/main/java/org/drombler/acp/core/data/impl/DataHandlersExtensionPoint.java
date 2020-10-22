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
package org.drombler.acp.core.data.impl;

import org.drombler.acp.core.application.ExtensionPoint;
import org.drombler.acp.core.data.jaxb.DataHandlersType;
import org.osgi.service.component.annotations.Component;

/**
 *
 * @author puce
 */
@Component
public class DataHandlersExtensionPoint implements ExtensionPoint<DataHandlersType> {

    @Override
    public Class<DataHandlersType> getJAXBRootClass() {
        return DataHandlersType.class;
    }
    
}
