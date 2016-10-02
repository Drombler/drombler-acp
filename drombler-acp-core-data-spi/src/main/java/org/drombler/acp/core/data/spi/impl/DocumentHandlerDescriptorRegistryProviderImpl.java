package org.drombler.acp.core.data.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistryProvider;
import org.drombler.commons.data.file.DocumentHandlerDescriptorRegistry;

/**
 *
 *
 * @author puce
 */
@Component
@Service
public class DocumentHandlerDescriptorRegistryProviderImpl implements DocumentHandlerDescriptorRegistryProvider {

    private final DocumentHandlerDescriptorRegistry documentHandlerDescriptorRegistry = new DocumentHandlerDescriptorRegistry();

    @Override
    public DocumentHandlerDescriptorRegistry getDocumentHandlerDescriptorRegistry() {
        return documentHandlerDescriptorRegistry;
    }
}
