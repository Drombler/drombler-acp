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

import org.drombler.acp.core.data.jaxb.DataHandlersType;
import org.drombler.acp.core.data.jaxb.DocumentHandlerType;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistryProvider;
import org.drombler.commons.data.file.DocumentHandlerDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
public class DocumentHandlerHandler extends AbstractDataHandlerHandler<DocumentHandlerDescriptor<?>> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentHandlerHandler.class);

    @Reference
    private DocumentHandlerDescriptorRegistryProvider documentHandlerDescriptorRegistryProvider;

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindDocumentHandlerDescriptor(DocumentHandlerDescriptor<?> handlerDescriptor) {
        resolveDataHandlerDescriptor(handlerDescriptor);
    }

    protected void unbindDocumentHandlerDescriptor(DocumentHandlerDescriptor<?> handlerDescriptor) {
        // TODO
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
            DocumentHandlerDescriptor<?> documentHandlerDescriptor = DataHandlerUtils.createDocumentHandlerDescriptor(
                    documentHandlerType, context.getBundle());
            resolveDataHandlerDescriptor(documentHandlerDescriptor);
        } catch (ClassNotFoundException | RuntimeException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    @Override
    protected boolean isInitialized() {
        return super.isInitialized() && documentHandlerDescriptorRegistryProvider != null;
    }

    @Override
    protected void resolveDataHandlerDescriptorInitialized(DocumentHandlerDescriptor<?> handlerDescriptor) {
        super.resolveDataHandlerDescriptorInitialized(handlerDescriptor);
        documentHandlerDescriptorRegistryProvider.getDocumentHandlerDescriptorRegistry().registerDocumentHandlerDescriptor(handlerDescriptor);
    }

}
