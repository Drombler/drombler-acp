package org.drombler.acp.core.data.spi;

import org.drombler.commons.data.file.FileExtensionDescriptorRegistry;

/**
 *
 * @author puce
 */


public interface FileExtensionDescriptorRegistryProvider {
    FileExtensionDescriptorRegistry getFileExtensionDescriptorRegistry();
}
