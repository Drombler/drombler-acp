package org.drombler.acp.core.data.spi;

public interface DocumentHandlerDescriptorRegistry {

    void registerDocumentHandlerDescriptor(DocumentHandlerDescriptor documentHandlerDescriptor);

    DocumentHandlerDescriptor getDocumentHandlerDescriptor(String mimeType);

    void registerListener();

    void unregisterListener();

}
