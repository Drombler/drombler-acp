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
package org.drombler.acp.startup.main;

import java.util.List;

/**
 * Provides the list of additional arguments passed to the application.
 *
 * Note: If this is a single instance application an new instance of this service is registered for every application start notification.
 *
 * With the Felix SCR annotations (Declarative Services) you can listen for multiplie instances with the following class annotation:
 *
 * {@code @Reference(name = "additionalArgumentsProvider", referenceInterface = AdditionalArgumentsProvider.class,
 * cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)}
 *
 * @author puce
 */
public interface AdditionalArgumentsProvider {

    /**
     * Gets the list of additional arguments passed to the application.
     *
     * @return the list of additional arguments passed to the application
     */
    List<String> getAdditionalArguments();
}