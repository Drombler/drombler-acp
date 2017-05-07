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

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistryProvider;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistryProvider;
import org.drombler.acp.startup.main.AdditionalArgumentsProvider;
import org.drombler.commons.data.file.DocumentHandlerDescriptor;
import org.drombler.commons.data.file.FileExtensionDescriptor;
import org.drombler.commons.data.file.FileUtils;
import org.osgi.service.component.ComponentContext;
import org.softsmithy.lib.util.SetChangeEvent;
import org.softsmithy.lib.util.SetChangeListener;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "additionalArgumentsProvider", referenceInterface = AdditionalArgumentsProvider.class,
        cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class AdditionalFileParamsHandler {

    @Reference
    private FileExtensionDescriptorRegistryProvider fileExtensionDescriptorRegistryProvider;
    @Reference
    private DocumentHandlerDescriptorRegistryProvider documentHandlerDescriptorRegistryProvider;
    @Reference
    private DataHandlerRegistryProvider dataHandlerRegistryProvider;

    private final List<String> unresolvedArguments = new ArrayList<>();
    private final List<Path> unresolvedPaths = new ArrayList<>();
    private final SetChangeListener<FileExtensionDescriptor> openFileListener = new OpenFileListener();
    private final SetChangeListener<DocumentHandlerDescriptor<?>> openDocumentListener = new OpenDocumentListener();

    protected void bindAdditionalArgumentsProvider(AdditionalArgumentsProvider additionalArgumentsProvider) {
        handleAdditionalArguments(additionalArgumentsProvider.getAdditionalArguments());
    }

    protected void unbindAdditionalArgumentsProvider(AdditionalArgumentsProvider additionalArgumentsProvider) {
        // TODO
    }

    protected void bindFileExtensionDescriptorRegistryProvider(FileExtensionDescriptorRegistryProvider fileExtensionDescriptorRegistryProvider) {
        this.fileExtensionDescriptorRegistryProvider = fileExtensionDescriptorRegistryProvider;
    }

    protected void unbindFileExtensionDescriptorRegistryProvider(FileExtensionDescriptorRegistryProvider fileExtensionDescriptorRegistry) {
        // TODO
    }

    protected void bindDocumentHandlerDescriptorRegistryProvider(DocumentHandlerDescriptorRegistryProvider documentHandlerDescriptorRegistryProvider) {
        this.documentHandlerDescriptorRegistryProvider = documentHandlerDescriptorRegistryProvider;
    }

    protected void unbindDocumentHandlerDescriptorRegistryProvider(DocumentHandlerDescriptorRegistryProvider documentHandlerDescriptorRegistryProvider) {
        // TODO
    }

    protected void bindDataHandlerRegistryProvider(DataHandlerRegistryProvider dataHandlerRegistryProvider) {
        this.dataHandlerRegistryProvider = dataHandlerRegistryProvider;
    }

    protected void unbindDataHandlerRegistryProvider(DataHandlerRegistryProvider dataHandlerRegistryProvider) {
        this.dataHandlerRegistryProvider = null;
    }

    @Activate
    protected void activate(ComponentContext context) {
        fileExtensionDescriptorRegistryProvider.getFileExtensionDescriptorRegistry().addFileExtensionListener(openFileListener);
        documentHandlerDescriptorRegistryProvider.getDocumentHandlerDescriptorRegistry().addDocumentHandlerDescriptorListener(openDocumentListener);
        resolveUnresolvedArguments();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        documentHandlerDescriptorRegistryProvider.getDocumentHandlerDescriptorRegistry().removeDocumentHandlerDescriptorListener(openDocumentListener);
        fileExtensionDescriptorRegistryProvider.getFileExtensionDescriptorRegistry().removeFileExtensionListener(openFileListener);
    }

    private boolean isInitialized() {
        return fileExtensionDescriptorRegistryProvider != null && documentHandlerDescriptorRegistryProvider != null
                && dataHandlerRegistryProvider != null;
    }

    private void handleAdditionalArguments(List<String> additionalArguments) {
        if (isInitialized()) {
            additionalArguments.stream()
                    .map(this::toPath)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(Files::isRegularFile)
                    .forEach(this::openFile);
        } else {
            unresolvedArguments.addAll(additionalArguments);
        }
    }

    private Optional<Path> toPath(String pathString) {
        try {
            return Optional.of(Paths.get(pathString));
        } catch (InvalidPathException ex1) {
            try {
                URI uri = new URI(pathString);
                return Optional.of(Paths.get(uri));
            } catch (URISyntaxException | NullPointerException | IllegalArgumentException | FileSystemNotFoundException | SecurityException ex2) {
                return Optional.empty();
            }
        }
    }

    private void openFile(Path filePath) {
        try {
            FileUtils.openFile(filePath, fileExtensionDescriptorRegistryProvider.getFileExtensionDescriptorRegistry(), documentHandlerDescriptorRegistryProvider.getDocumentHandlerDescriptorRegistry(),
                    dataHandlerRegistryProvider.getDataHandlerRegistry());
        } catch (RuntimeException ex) {
            unresolvedPaths.add(filePath);
        }
    }

    private void resolveUnresolvedArguments() {
        List<String> unresolvedArgumentsCopy = new ArrayList<>(unresolvedArguments);
        unresolvedArguments.clear();
        handleAdditionalArguments(unresolvedArgumentsCopy);
    }

    private void resolveUnresolvedPaths() {
        List<Path> unresolvedPathsCopy = new ArrayList<>(unresolvedPaths);
        unresolvedPaths.clear();
        unresolvedPathsCopy.forEach(this::openFile);
    }

    private class OpenFileListener implements SetChangeListener<FileExtensionDescriptor> {

        @Override
        public void elementAdded(SetChangeEvent<FileExtensionDescriptor> event) {
            resolveUnresolvedPaths();
        }

        @Override
        public void elementRemoved(SetChangeEvent<FileExtensionDescriptor> event) {
            // nothing to do
        }

    }

    private class OpenDocumentListener implements SetChangeListener<DocumentHandlerDescriptor<?>> {

        @Override
        public void elementAdded(SetChangeEvent<DocumentHandlerDescriptor<?>> event) {
            resolveUnresolvedPaths();
        }

        @Override
        public void elementRemoved(SetChangeEvent<DocumentHandlerDescriptor<?>> event) {
            // nothing to do
        }

    }
}
