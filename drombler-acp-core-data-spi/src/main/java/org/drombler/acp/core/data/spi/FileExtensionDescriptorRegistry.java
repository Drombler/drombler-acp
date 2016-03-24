package org.drombler.acp.core.data.spi;

public interface FileExtensionDescriptorRegistry {

    void registerFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor);

    FileExtensionDescriptor getFileExtensionDescriptor(String fileExtension);

    void registerListener();

    void unregisterListener();

}
