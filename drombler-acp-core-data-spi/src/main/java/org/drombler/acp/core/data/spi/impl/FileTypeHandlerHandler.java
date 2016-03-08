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
import org.drombler.acp.core.data.jaxb.FileTypeHandlerType;
import org.drombler.acp.core.data.jaxb.FileTypeHandlersType;
import org.drombler.acp.core.data.spi.FileTypeHandlerDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "fileTypeHandlersType", referenceInterface = FileTypeHandlersType.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "fileTypeHandlerDescriptor", referenceInterface = FileTypeHandlerDescriptor.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
})
public class FileTypeHandlerHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FileTypeHandlerHandler.class);

    private final List<FileTypeHandlerDescriptor> unresolvedFileTypeHandlerDescriptors = new ArrayList<>();
    private final FileTypeHandlerRegistry fileTypeHandlerRegistry = new FileTypeHandlerRegistry();

    protected void bindFileTypeHandlersType(ServiceReference<FileTypeHandlersType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        FileTypeHandlersType fileTypeHandlersType = context.getService(serviceReference);
        registerFileTypeHandlers(fileTypeHandlersType, context);
    }

    protected void unbindFileTypeHandlersType(FileTypeHandlersType fileTypeHandlersType) {
        // TODO
    }

    protected void bindFileTypeHandlerDescriptor(FileTypeHandlerDescriptor handlerDescriptor) {
        resolveFileTypeHandlerDescriptor(handlerDescriptor);
    }

    protected void unbindFileTypeHandlerDescriptor(FileTypeHandlerDescriptor handlerDescriptor) {
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedFileTypeHandlerDescriptors();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    private void registerFileTypeHandlers(FileTypeHandlersType fileTypeHandlersType, BundleContext context) {
        fileTypeHandlersType.getFileTypeHandler().forEach(fileTypeHandlerType -> registerFileTypeHandler(
                fileTypeHandlerType, context));
    }

    private void registerFileTypeHandler(FileTypeHandlerType fileTypeHandlerType, BundleContext context) {
        try {
            // TODO: register FileTypeHandlerDescriptor as service?
            resolveFileTypeHandlerDescriptor(FileTypeHandlerDescriptor.createFileTypeHandlerDescriptor(
                    fileTypeHandlerType, context.getBundle()));
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

    private void resolveFileTypeHandlerDescriptor(FileTypeHandlerDescriptor handlerDescriptor) {
        if (isInitialized()) {
            fileTypeHandlerRegistry.registerFileTypeHandler(handlerDescriptor);
        } else {
            unresolvedFileTypeHandlerDescriptors.add(handlerDescriptor);
        }
    }

    private void resolveUnresolvedFileTypeHandlerDescriptors() {
        unresolvedFileTypeHandlerDescriptors.forEach(this::resolveFileTypeHandlerDescriptor);
    }

    private boolean isInitialized() {
        return true;
    }

}
