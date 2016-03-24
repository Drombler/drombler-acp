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

import java.util.ArrayList;
import java.util.List;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.drombler.acp.core.data.jaxb.DocumentHandlerType;
import org.drombler.acp.core.data.jaxb.DocumentHandlersType;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistry;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "documentHandlersType", referenceInterface = DocumentHandlersType.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "documentHandlerDescriptor", referenceInterface = DocumentHandlerDescriptor.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
})
public class DocumentHandlerHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentHandlerHandler.class);

    private final List<DocumentHandlerDescriptor> unresolvedDocumentHandlerDescriptors = new ArrayList<>();

    @Reference
    private DocumentHandlerDescriptorRegistry documentHandlersTypeRegistry;

    protected void bindDocumentHandlersType(ServiceReference<DocumentHandlersType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        DocumentHandlersType documentHandlersType = context.getService(serviceReference);
        registerDocumentHandlers(documentHandlersType, context);
    }

    protected void unbindDocumentHandlersType(DocumentHandlersType documentHandlersType) {
        // TODO
    }

    protected void bindDocumentHandlerDescriptor(DocumentHandlerDescriptor handlerDescriptor) {
        resolveDocumentHandlerDescriptor(handlerDescriptor);
    }

    protected void unbindDocumentHandlerDescriptor(DocumentHandlerDescriptor handlerDescriptor) {
    }

    protected void bindDocumentHandlerRegistry(DocumentHandlerDescriptorRegistry documentHandlerRegistry) {
        this.documentHandlersTypeRegistry = documentHandlerRegistry;
    }

    protected void unbindDocumentHandlerRegistry(DocumentHandlerDescriptorRegistry documentHandlerRegistry) {
        // TODO
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedDocumentHandlerDescriptors();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    private void registerDocumentHandlers(DocumentHandlersType documentHandlersType, BundleContext context) {
        documentHandlersType.getDocumentHandler().forEach(documentHandlerType
                -> registerDocumentHandler(documentHandlerType, context));
    }

    private void registerDocumentHandler(DocumentHandlerType documentHandlerType, BundleContext context) {
        try {
            // TODO: register DocumentHandlerDescriptor as service?
            DocumentHandlerDescriptor documentHandlerDescriptor = DocumentHandlerDescriptor.createDocumentTypeHandlerDescriptor(
                    documentHandlerType, context.getBundle());
            resolveDocumentHandlerDescriptor(documentHandlerDescriptor);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
//    protected void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context) {
//        dockingsType.getEditorDocking().forEach(dockingType -> {
//            try {
//                EditorDockingDescriptor dockingDescriptor = EditorDockingDescriptor.createEditorDockingDescriptor(
//                        dockingType, bundle);
//                // TODO: register EditorDockingDescriptor as service? Omit resolveDockingDescriptor?
//                resolveDockingDescriptor(dockingDescriptor);
//            } catch (Exception ex) {
//                LOG.error(ex.getMessage(), ex);
//            }
//        });
//    }

    private void resolveDocumentHandlerDescriptor(DocumentHandlerDescriptor handlerDescriptor) {
        if (isInitialized()) {
            documentHandlersTypeRegistry.registerDocumentHandlerDescriptor(handlerDescriptor);
        } else {
            unresolvedDocumentHandlerDescriptors.add(handlerDescriptor);
        }
    }

    private void resolveUnresolvedDocumentHandlerDescriptors() {
        unresolvedDocumentHandlerDescriptors.forEach(this::resolveDocumentHandlerDescriptor);
    }

    private boolean isInitialized() {
        return documentHandlersTypeRegistry != null;
    }

}
