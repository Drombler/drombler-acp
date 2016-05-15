package org.drombler.acp.core.data.spi;

public interface DocumentHandlerDescriptorRegistry {

    void registerDocumentHandlerDescriptor(DocumentHandlerDescriptor<?> documentHandlerDescriptor);

    void unregisterDocumentHandlerDescriptor(DocumentHandlerDescriptor<?> documentHandlerDescriptor);

    DocumentHandlerDescriptor<?> getDocumentHandlerDescriptor(String mimeType);

    void registerDocumentHandlerDescriptorListener(DocumentHandlerDescriptorListener listener);

    void unregisterDocumentHandlerDescriptorListener(DocumentHandlerDescriptorListener listener);

}
