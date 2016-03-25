package org.drombler.acp.core.data.spi;

public interface DocumentHandlerDescriptorRegistry {

    void registerDocumentHandlerDescriptor(DocumentHandlerDescriptor<?> documentHandlerDescriptor);

    void unregisterDocumentHandlerDescriptor(DocumentHandlerDescriptor<?> documentHandlerDescriptor);

    DocumentHandlerDescriptor<?> getDocumentHandlerDescriptor(String mimeType);

    DocumentHandlerDescriptor<?> getDocumentHandlerDescriptor(Object documentHandler);

    void registerListener();

    void unregisterListener();

}
