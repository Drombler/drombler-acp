/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (GitHub user: puce77).
 * Copyright 2016 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.data.spi.impl;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.drombler.acp.core.data.jaxb.DataHandlersType;
import org.drombler.acp.core.data.jaxb.DocumentHandlerType;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptor;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "documentHandlerDescriptor", referenceInterface = DocumentHandlerDescriptor.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
})
public class DocumentHandlerHandler<D> extends AbstractDataHandlerHandler<D, DocumentHandlerDescriptor<D>> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentHandlerHandler.class);

    @Reference
    private DocumentHandlerDescriptorRegistry documentHandlerDescriptorRegistry;

    protected void bindDocumentHandlerDescriptor(DocumentHandlerDescriptor handlerDescriptor) {
        resolveDataHandlerDescriptor(handlerDescriptor);
    }

    protected void unbindDocumentHandlerDescriptor(DocumentHandlerDescriptor handlerDescriptor) {
    }

    protected void bindDocumentHandlerRegistry(DocumentHandlerDescriptorRegistry documentHandlerRegistry) {
        this.documentHandlerDescriptorRegistry = documentHandlerRegistry;
    }

    protected void unbindDocumentHandlerRegistry(DocumentHandlerDescriptorRegistry documentHandlerRegistry) {
        this.documentHandlerDescriptorRegistry = null;
    }

    @Activate
    @Override
    protected void activate(ComponentContext context) {
        super.activate(context);
    }

    @Deactivate
    @Override
    protected void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    @Override
    protected void registerDataHandlers(DataHandlersType dataHandlersType, BundleContext context) {
        dataHandlersType.getDocumentHandler().forEach(documentHandlerType
                -> registerDocumentHandler(documentHandlerType, context));
    }

    private void registerDocumentHandler(DocumentHandlerType documentHandlerType, BundleContext context) {
        try {
            // TODO: register DocumentHandlerDescriptor as service?
            DocumentHandlerDescriptor documentHandlerDescriptor = DocumentHandlerDescriptor.createDocumentHandlerDescriptor(
                    documentHandlerType, context.getBundle());
            resolveDataHandlerDescriptor(documentHandlerDescriptor);
        } catch (ClassNotFoundException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && documentHandlerDescriptorRegistry != null;
    }

    @Override
    protected void resolveDataHandlerDescriptorInitialized(DocumentHandlerDescriptor<D> handlerDescriptor) {
        super.resolveDataHandlerDescriptorInitialized(handlerDescriptor);
        documentHandlerDescriptorRegistry.registerDocumentHandlerDescriptor(handlerDescriptor);
    }

}
