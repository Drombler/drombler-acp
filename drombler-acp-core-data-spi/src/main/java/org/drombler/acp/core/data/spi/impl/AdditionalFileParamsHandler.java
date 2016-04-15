/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2012 Drombler.org. All Rights Reserved.
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
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistry;
import org.drombler.acp.core.data.spi.DocumentHandlerEvent;
import org.drombler.acp.core.data.spi.DocumentHandlerListener;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistry;
import org.drombler.acp.core.data.spi.FileExtensionEvent;
import org.drombler.acp.core.data.spi.FileExtensionListener;
import org.drombler.acp.core.data.spi.FileUtils;
import org.drombler.acp.startup.main.AdditionalArgumentsProvider;
import org.osgi.service.component.ComponentContext;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "additionalArgumentsProvider", referenceInterface = AdditionalArgumentsProvider.class,
        cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class AdditionalFileParamsHandler {

    @Reference
    private FileExtensionDescriptorRegistry fileExtensionDescriptorRegistry;
    @Reference
    private DocumentHandlerDescriptorRegistry documentHandlerDescriptorRegistry;

    private final List<String> unresolvedArguments = new ArrayList<>();
    private final List<Path> unresolvedPaths = new ArrayList<>();
    private final FileExtensionListener fileExtensionListener = new OpenFileListener();
    private final DocumentHandlerListener documentHandlerListener = new OpenDocumentListener();

    protected void bindAdditionalArgumentsProvider(AdditionalArgumentsProvider additionalArgumentsProvider) {
        handleAdditionalArguments(additionalArgumentsProvider.getAdditionalArguments());
    }

    protected void unbindAdditionalArgumentsProvider(AdditionalArgumentsProvider additionalArgumentsProvider) {
        // TODO
    }

    protected void bindFileExtensionDescriptorRegistry(FileExtensionDescriptorRegistry fileExtensionDescriptorRegistry) {
        this.fileExtensionDescriptorRegistry = fileExtensionDescriptorRegistry;
    }

    protected void unbindFileExtensionDescriptorRegistry(FileExtensionDescriptorRegistry fileExtensionDescriptorRegistry) {
        // TODO
    }

    protected void bindDocumentHandlerDescriptorRegistry(DocumentHandlerDescriptorRegistry documentHandlerDescriptorRegistry) {
        this.documentHandlerDescriptorRegistry = documentHandlerDescriptorRegistry;
    }

    protected void unbindDocumentHandlerDescriptorRegistry(DocumentHandlerDescriptorRegistry documentHandlerDescriptorRegistry) {
        // TODO
    }

    @Activate
    protected void activate(ComponentContext context) {
        fileExtensionDescriptorRegistry.registerFileExtensionListener(fileExtensionListener);
        documentHandlerDescriptorRegistry.registerDocumentHandlerListener(documentHandlerListener);
        resolveUnresolvedArguments();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        documentHandlerDescriptorRegistry.unregisterDocumentHandlerListener(documentHandlerListener);
        fileExtensionDescriptorRegistry.unregisterFileExtensionListener(fileExtensionListener);
    }

    private boolean isInitialized() {
        return fileExtensionDescriptorRegistry != null && documentHandlerDescriptorRegistry != null;
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
            FileUtils.openFile(filePath, fileExtensionDescriptorRegistry, documentHandlerDescriptorRegistry);
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

    private class OpenFileListener implements FileExtensionListener {

        @Override
        public void fileExtensionAdded(FileExtensionEvent event) {
            resolveUnresolvedPaths();
        }

        @Override
        public void fileExtensionRemoved(FileExtensionEvent event) {
            // nothing to do
        }

    }

    private class OpenDocumentListener implements DocumentHandlerListener {

        @Override
        public void documentHandlerAdded(DocumentHandlerEvent<?> event) {
            resolveUnresolvedPaths();
        }

        @Override
        public void documentHandlerRemoved(DocumentHandlerEvent<?> event) {
            // nothing to do
        }

    }
}
