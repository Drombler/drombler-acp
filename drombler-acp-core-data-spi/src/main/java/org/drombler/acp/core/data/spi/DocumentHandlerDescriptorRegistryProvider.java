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
package org.drombler.acp.core.data.spi;

import org.drombler.commons.data.file.DocumentHandlerDescriptorRegistry;

/**
 * A {@link DocumentHandlerDescriptorRegistry} provider.<br>
 * <br>
 * This module also provides an implementation registered as OSGi service.
 *
 * @author puce
 */
public interface DocumentHandlerDescriptorRegistryProvider {

    /**
     * Gets a {@link DocumentHandlerDescriptorRegistry}.
     *
     * @return a DocumentHandlerDescriptorRegistry
     */
    DocumentHandlerDescriptorRegistry getDocumentHandlerDescriptorRegistry();
}
