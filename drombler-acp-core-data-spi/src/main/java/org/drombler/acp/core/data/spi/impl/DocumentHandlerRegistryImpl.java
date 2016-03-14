package org.drombler.acp.core.data.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptor;
import org.drombler.acp.core.data.spi.DocumentHandlerRegistry;

/**
 *
 *
 * @author puce
 */
@Component
@Service
public class DocumentHandlerRegistryImpl implements DocumentHandlerRegistry {

    @Override
    public void registerDocumentHandler(DocumentHandlerDescriptor handlerDescriptor) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
