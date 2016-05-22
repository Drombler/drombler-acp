package org.drombler.acp.core.data.spi;

import java.util.Set;

public interface FileExtensionDescriptorRegistry {

    void registerFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor);

    void unregisterFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor);

    FileExtensionDescriptor getFileExtensionDescriptor(String fileExtension);

    Set<FileExtensionDescriptor> getAllFileExtensionDescriptors();

    void registerFileExtensionListener(FileExtensionListener listener);

    void unregisterFileExtensionListener(FileExtensionListener listener);

}
