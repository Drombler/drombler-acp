package org.drombler.acp.core.data.spi.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptor;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistry;

/**
 *
 *
 * @author puce
 */
@Component
@Service
public class DocumentHandlerRegistryImpl implements DocumentHandlerDescriptorRegistry {

    private final Map<String, DocumentHandlerDescriptor> documentHandlerDescriptors = new HashMap<>();

    @Override
    public void registerDocumentHandlerDescriptor(DocumentHandlerDescriptor documentHandlerDescriptor) {
        documentHandlerDescriptors.put(documentHandlerDescriptor.getMimeType().toLowerCase(), documentHandlerDescriptor);
    }

    @Override
    public DocumentHandlerDescriptor getDocumentHandlerDescriptor(String mimeType) {
        return documentHandlerDescriptors.get(mimeType.toLowerCase());
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
