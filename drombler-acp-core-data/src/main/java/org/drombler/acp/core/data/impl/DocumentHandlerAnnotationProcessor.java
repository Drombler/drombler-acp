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
import org.drombler.acp.core.data.DocumentHandler;
import org.drombler.acp.core.data.jaxb.DocumentHandlerType;
import org.drombler.acp.core.data.jaxb.DocumentHandlersType;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({
    "org.drombler.acp.core.data.DocumentHandler"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DocumentHandlerAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private DocumentHandlersType documentHandlers;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(DocumentHandler.class).forEach(element -> {
            DocumentHandler documentHandlerAnnotation = element.getAnnotation(DocumentHandler.class);
            if (documentHandlerAnnotation != null) {
                registerDocumentTypeHandlerAnnotation(element, documentHandlerAnnotation);
            }
        });

        return false;
    }

    private void init(Element element) {
        if (documentHandlers == null) {
            documentHandlers = new DocumentHandlersType();
            addExtensionConfigurations(documentHandlers);
            addJAXBRootClasses(DocumentHandlersType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

    private void registerDocumentTypeHandlerAnnotation(Element element,
            DocumentHandler documentTypeHandlerAnnotation) {
        init(element);

        DocumentHandlerType documentTypeHandler = new DocumentHandlerType();
        configureDocumentTypeHandler(documentTypeHandler, documentTypeHandlerAnnotation.mimeType(), documentTypeHandlerAnnotation.icon(), element);
        documentHandlers.getDocumentHandler().add(documentTypeHandler);
    }

    private void configureDocumentTypeHandler(DocumentHandlerType documentHandler, String mimeType,
            String icon, Element element) {
        documentHandler.setMimeType(StringUtils.stripToNull(mimeType));
        documentHandler.setIcon(StringUtils.stripToNull(icon));
        documentHandler.setHandlerClass(element.asType().toString());
    }
}
