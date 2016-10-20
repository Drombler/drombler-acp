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
package org.drombler.acp.core.data.impl;

import java.util.Arrays;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.application.AbstractApplicationAnnotationProcessor;
import org.drombler.acp.core.data.FileExtension;
import org.drombler.acp.core.data.FileExtensions;
import org.drombler.acp.core.data.jaxb.FileExtensionType;
import org.drombler.acp.core.data.jaxb.FileExtensionsType;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({
    "org.drombler.acp.core.data.FileExtensions",
    "org.drombler.acp.core.data.FileExtension"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class FileExtensionAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private FileExtensionsType fileExtensions;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(FileExtensions.class).forEach(element -> {
            FileExtensions fileExtensionsAnnotation = element.getAnnotation(FileExtensions.class);
            if (fileExtensionsAnnotation != null) {
                for (FileExtension fileExtensionAnnotation : fileExtensionsAnnotation.value()) {
                    registerFileExtensionAnnotation(fileExtensionAnnotation, element);
                }
            }
        });

        roundEnv.getElementsAnnotatedWith(FileExtension.class).forEach(element -> {
            FileExtension fileExtensionAnnotation = element.getAnnotation(FileExtension.class);
            if (fileExtensionAnnotation != null) {
                registerFileExtensionAnnotation(fileExtensionAnnotation, element);
            }
        });

        return false;
    }

    private void init(Element element) {
        if (fileExtensions == null) {
            fileExtensions = new FileExtensionsType();
            addExtensionConfigurations(fileExtensions);
            addJAXBRootClasses(FileExtensionsType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

    private void registerFileExtensionAnnotation(FileExtension fileExtensionAnnotation, Element element) {
        init(element);

        FileExtensionType fileExtension = new FileExtensionType();
        configureFileExtension(fileExtension, fileExtensionAnnotation.mimeType(), fileExtensionAnnotation.displayName(),
                fileExtensionAnnotation.fileExtensions(), element);
        fileExtensions.getFileExtension().add(fileExtension);
    }

    private void configureFileExtension(FileExtensionType fileExtension, String mimeType,
            String displayName, String[] fileExtensions, Element element) {
        fileExtension.setDisplayName(StringUtils.stripToNull(displayName));
        fileExtension.setMimeType(StringUtils.stripToNull(mimeType));
        fileExtension.getFileExtension().addAll(Arrays.asList(fileExtensions));
        fileExtension.setPackage(element.asType().toString());
    }
}
