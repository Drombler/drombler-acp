package org.drombler.acp.core.data.spi.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptor;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistry;
import org.drombler.acp.core.data.spi.DocumentHandlerEvent;
import org.drombler.acp.core.data.spi.DocumentHandlerListener;

/**
 *
 *
 * @author puce
 */
@Component
@Service
public class DocumentHandlerDescriptorRegistryImpl implements DocumentHandlerDescriptorRegistry {

    private final Map<String, DocumentHandlerDescriptor<?>> mimeTypes = new HashMap<>();
    private final Map<Class<?>, DocumentHandlerDescriptor<?>> documentHandlerClasses = new HashMap<>();
    private final Set<DocumentHandlerListener> listeners = new HashSet<>();

    @Override
    public void registerDocumentHandlerDescriptor(DocumentHandlerDescriptor<?> documentHandlerDescriptor) {
        mimeTypes.put(documentHandlerDescriptor.getMimeType().toLowerCase(), documentHandlerDescriptor);
        documentHandlerClasses.put(documentHandlerDescriptor.getDocumentHandlerClass(), documentHandlerDescriptor);
        fireDocumentHandlerAdded(documentHandlerDescriptor);
    }

    @Override
    public void unregisterDocumentHandlerDescriptor(DocumentHandlerDescriptor<?> documentHandlerDescriptor) {
        mimeTypes.remove(documentHandlerDescriptor.getMimeType().toLowerCase(), documentHandlerDescriptor);
        documentHandlerClasses.remove(documentHandlerDescriptor.getDocumentHandlerClass(), documentHandlerDescriptor);
        fireDocumentHandlerRemoved(documentHandlerDescriptor);
    }

    @Override
    public DocumentHandlerDescriptor<?> getDocumentHandlerDescriptor(String mimeType) {
        return mimeTypes.get(mimeType.toLowerCase());
    }

    @Override
    public DocumentHandlerDescriptor<?> getDocumentHandlerDescriptor(Object documentHandler) {
        return documentHandlerClasses.get(documentHandler.getClass());
    }

    @Override
    public void registerDocumentHandlerListener(DocumentHandlerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterDocumentHandlerListener(DocumentHandlerListener listener) {
        listeners.remove(listener);
    }

    private <D> void fireDocumentHandlerAdded(DocumentHandlerDescriptor<D> documentHandlerDescriptor) {
        DocumentHandlerEvent<D> event = new DocumentHandlerEvent<>(this, documentHandlerDescriptor);
        listeners.forEach(listener -> listener.documentHandlerAdded(event));
    }

    private <D> void fireDocumentHandlerRemoved(DocumentHandlerDescriptor<D> documentHandlerDescriptor) {
        DocumentHandlerEvent<D> event = new DocumentHandlerEvent<>(this, documentHandlerDescriptor);
        listeners.forEach(listener -> listener.documentHandlerRemoved(event));
    }
}
