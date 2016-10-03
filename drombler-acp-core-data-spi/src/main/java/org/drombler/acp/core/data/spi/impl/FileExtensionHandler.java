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
import org.drombler.acp.core.data.jaxb.FileExtensionType;
import org.drombler.acp.core.data.jaxb.FileExtensionsType;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistryProvider;
import org.drombler.commons.data.file.FileExtensionDescriptor;
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
    @Reference(name = "fileExtensionsType", referenceInterface = FileExtensionsType.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "fileExtensionDescriptor", referenceInterface = FileExtensionDescriptor.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
})
public class FileExtensionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FileExtensionHandler.class);

    private final List<FileExtensionDescriptor> unresolvedFileExtensionDescriptors = new ArrayList<>();

    @Reference
    private FileExtensionDescriptorRegistryProvider fileExtensionDescriptorRegistryProvider;

    protected void bindFileExtensionsType(ServiceReference<FileExtensionsType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        FileExtensionsType fileExtensionsType = context.getService(serviceReference);
        registerFileExtensions(fileExtensionsType, context);
    }

    protected void unbindFileExtensionsType(FileExtensionsType fileExtensionsType) {
        // TODO
    }

    protected void bindFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor) {
        registerFileExtensionDescriptor(fileExtensionDescriptor);
    }

    protected void unbindFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor) {
        // TODO
    }

    protected void bindFileExtensionDescriptorRegistryProvider(FileExtensionDescriptorRegistryProvider fileExtensionDescriptorRegistryProvider) {
        this.fileExtensionDescriptorRegistryProvider = fileExtensionDescriptorRegistryProvider;
    }

    protected void unbindFileExtensionDescriptorRegistryProvider(FileExtensionDescriptorRegistryProvider fileExtensionDescriptorRegistryProvider) {
        // TODO
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedFileExtensionDescriptors();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    private void registerFileExtensions(FileExtensionsType fileExtensionsType, BundleContext context) {
        fileExtensionsType.getFileExtension().forEach(fileExtensionType
                -> registerFileExtension(fileExtensionType, context));
    }

    private void registerFileExtension(FileExtensionType fileExtensionType, BundleContext context) {
        // TODO: register FileExtensionDescriptor as service?
        FileExtensionDescriptor fileExtensionDescriptor = FileExtensionUtils.createFileExtensionDescriptor(
                fileExtensionType, context.getBundle());
        registerFileExtensionDescriptor(fileExtensionDescriptor);
    }

    private void registerFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor) {
        if (isInitialized()) {
            fileExtensionDescriptorRegistryProvider.getFileExtensionDescriptorRegistry().registerFileExtensionDescriptor(fileExtensionDescriptor);
        } else {
            unresolvedFileExtensionDescriptors.add(fileExtensionDescriptor);
        }
    }

    private void resolveUnresolvedFileExtensionDescriptors() {
        unresolvedFileExtensionDescriptors.forEach(this::registerFileExtensionDescriptor);
    }

    private boolean isInitialized() {
        return fileExtensionDescriptorRegistryProvider != null;
    }
}
