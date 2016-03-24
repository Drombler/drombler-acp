package org.drombler.acp.core.data.spi.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.data.spi.FileExtensionDescriptor;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistry;

/**
 *
 * @author puce
 */
@Component
@Service
public class FileExtensionRegistryImpl implements FileExtensionDescriptorRegistry {
    private final Map<String, FileExtensionDescriptor> fileExtensionDescriptors = new HashMap<>();

    @Override
    public void registerFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor) {
        fileExtensionDescriptor.getFileExtensions().stream()
                .map(String::toLowerCase)
                .forEach(fileExtension -> fileExtensionDescriptors.put(fileExtension, fileExtensionDescriptor));
    }

    @Override
    public FileExtensionDescriptor getFileExtensionDescriptor(String fileExtension) {
        return fileExtensionDescriptors.get(fileExtension.toLowerCase());
    }

    @Override
    public void registerListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unregisterListener() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
