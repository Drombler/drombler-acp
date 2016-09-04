package org.drombler.acp.core.data.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.commons.data.file.FileExtensionDescriptorRegistry;
import org.drombler.commons.data.file.SimpleFileExtensionDescriptorRegistry;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistryProvider;

/**
 *
 * @author puce
 */
@Component
@Service
public class FileExtensionDescriptorRegistryProviderImpl implements FileExtensionDescriptorRegistryProvider {

    private final FileExtensionDescriptorRegistry fileExtensionDescriptorRegistry = new SimpleFileExtensionDescriptorRegistry();

    @Override
    public FileExtensionDescriptorRegistry getFileExtensionDescriptorRegistry() {
        return fileExtensionDescriptorRegistry;
    }
}
