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
import org.drombler.acp.core.application.processing.AbstractApplicationAnnotationProcessor;
import org.drombler.acp.core.data.BusinessObjectHandler;
import org.drombler.acp.core.data.DocumentHandler;
import org.drombler.acp.core.data.jaxb.BusinessObjectHandlerType;
import org.drombler.acp.core.data.jaxb.DataHandlerType;
import org.drombler.acp.core.data.jaxb.DataHandlersType;
import org.drombler.acp.core.data.jaxb.DocumentHandlerType;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({
    "org.drombler.acp.core.data.DocumentHandler",
    "org.drombler.acp.core.data.BusinessObjectHandler"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DataHandlerAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private DataHandlersType dataHandlers;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(DocumentHandler.class).forEach(element -> {
            DocumentHandler documentHandlerAnnotation = element.getAnnotation(DocumentHandler.class);
            if (documentHandlerAnnotation != null) {
                registerDocumentTypeHandlerAnnotation(element, documentHandlerAnnotation);
            }
        });

        roundEnv.getElementsAnnotatedWith(BusinessObjectHandler.class).forEach(element -> {
            BusinessObjectHandler businessObjectHandlerAnnotation = element.getAnnotation(BusinessObjectHandler.class);
            if (businessObjectHandlerAnnotation != null) {
                registerBusinessObjectHandlerAnnotation(element, businessObjectHandlerAnnotation);
            }
        });

        return false;
    }

    private void init(Element element) {
        if (dataHandlers == null) {
            dataHandlers = new DataHandlersType();
            addExtensionConfigurations(dataHandlers);
            addJAXBRootClasses(DataHandlersType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

    private void registerDocumentTypeHandlerAnnotation(Element element, DocumentHandler documentTypeHandlerAnnotation) {
        init(element);

        DocumentHandlerType documentTypeHandler = new DocumentHandlerType();
        configureDocumentTypeHandler(documentTypeHandler, documentTypeHandlerAnnotation.mimeType(), documentTypeHandlerAnnotation.icon(), element);
        dataHandlers.getDocumentHandler().add(documentTypeHandler);
    }

    private void configureDocumentTypeHandler(DocumentHandlerType documentHandler, String mimeType, String icon, Element element) {
        documentHandler.setMimeType(StringUtils.stripToNull(mimeType));
        configureDataTypeHandler(documentHandler, icon, element);
    }

    private void registerBusinessObjectHandlerAnnotation(Element element, BusinessObjectHandler businessObjectHandlerAnnotation) {
        init(element);

        BusinessObjectHandlerType businessObjectHandler = new BusinessObjectHandlerType();
        configureDataTypeHandler(businessObjectHandler, businessObjectHandlerAnnotation.icon(), element);
        dataHandlers.getBusinessObjectHandler().add(businessObjectHandler);
    }

    private void configureDataTypeHandler(DataHandlerType dataHandlerType, String icon, Element element) {
        dataHandlerType.setIcon(StringUtils.stripToNull(icon));
        dataHandlerType.setHandlerClass(element.asType().toString());
    }
}
