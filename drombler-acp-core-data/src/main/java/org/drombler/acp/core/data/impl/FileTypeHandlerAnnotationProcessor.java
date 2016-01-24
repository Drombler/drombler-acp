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

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.application.AbstractApplicationAnnotationProcessor;
import org.drombler.acp.core.data.FileTypeHandler;
import org.drombler.acp.core.data.jaxb.FileTypeHandlerType;
import org.drombler.acp.core.data.jaxb.FileTypeHandlersType;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({
    "org.drombler.acp.core.data.FileTypeHandler"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class FileTypeHandlerAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private FileTypeHandlersType fileTypeHandlers;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(FileTypeHandler.class).stream().
                forEach(element -> {
                    FileTypeHandler fileTypeHandlerAnnotation = element.getAnnotation(FileTypeHandler.class);
                    if (fileTypeHandlerAnnotation != null) {
                        registerFileTypeHandlerAnnotation(element, fileTypeHandlerAnnotation);
                    }
                });

        return false;
    }

    private void init(Element element) {
        if (fileTypeHandlers == null) {
            fileTypeHandlers = new FileTypeHandlersType();
            addExtensionConfigurations(fileTypeHandlers);
            addJAXBRootClasses(FileTypeHandlersType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

    private void registerFileTypeHandlerAnnotation(Element element, FileTypeHandler fileTypeHandlerAnnotation) {
        init(element);

        FileTypeHandlerType fileTypeHandler = new FileTypeHandlerType();
        configureFileTypeHandler(fileTypeHandler, fileTypeHandlerAnnotation.mimeType(),
                fileTypeHandlerAnnotation.displayName(), fileTypeHandlerAnnotation.icon(), element);
        fileTypeHandlers.getFileTypeHandler().add(fileTypeHandler);
    }

    private void configureFileTypeHandler(FileTypeHandlerType fileTypeHandler, String mimeType, String displayName,
            String icon, Element element) {
        fileTypeHandler.setMimeType(StringUtils.stripToNull(mimeType));
        fileTypeHandler.setDisplayName(StringUtils.stripToNull(displayName));
        fileTypeHandler.setIcon(StringUtils.stripToNull(icon));
        fileTypeHandler.setHandlerClass(element.asType().toString());
    }
}
