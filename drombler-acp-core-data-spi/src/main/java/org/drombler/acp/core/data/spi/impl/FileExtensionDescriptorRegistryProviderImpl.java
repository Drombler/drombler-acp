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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistryProvider;
import org.drombler.commons.data.file.FileExtensionDescriptorRegistry;

/**
 *
 * @author puce
 */
@Component
@Service
public class FileExtensionDescriptorRegistryProviderImpl implements FileExtensionDescriptorRegistryProvider {

    private final FileExtensionDescriptorRegistry fileExtensionDescriptorRegistry = new FileExtensionDescriptorRegistry();

    @Override
    public FileExtensionDescriptorRegistry getFileExtensionDescriptorRegistry() {
        return fileExtensionDescriptorRegistry;
    }
}
