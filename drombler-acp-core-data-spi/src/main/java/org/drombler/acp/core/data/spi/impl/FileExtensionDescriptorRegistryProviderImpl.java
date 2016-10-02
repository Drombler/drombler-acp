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
