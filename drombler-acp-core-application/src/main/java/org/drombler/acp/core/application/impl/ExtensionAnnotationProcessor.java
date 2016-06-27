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
package org.drombler.acp.core.application.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.drombler.acp.core.application.AbstractApplicationAnnotationProcessor;
import org.drombler.acp.core.application.Extension;
import org.drombler.acp.core.application.Extensions;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({
    "org.drombler.acp.core.application.Extensions",
    "org.drombler.acp.core.application.Extension"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ExtensionAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Extensions.class).forEach(element -> {
            Extensions extensionsAnnotation = element.getAnnotation(Extensions.class);
            if (extensionsAnnotation != null) {
                for (Extension extensionAnnotation : extensionsAnnotation.value()) {
                    registerExtensionAnnotation(extensionAnnotation, element);
                }
            }
        });

        roundEnv.getElementsAnnotatedWith(Extension.class).forEach(element -> {
            Extension extensionAnnotation = element.getAnnotation(Extension.class);
            if (extensionAnnotation != null) {
                registerExtensionAnnotation(extensionAnnotation, element);
            }
        });

        return false;
    }

    private void init(Extension extensionAnnotation, Element element) {
        TypeMirror extensionJAXBRootClass = getExtensionJAXBRootClass(extensionAnnotation);
        addJAXBPackage(getPackageName(extensionJAXBRootClass));
        addOriginatingElements(element); // TODO: needed?
    }

    private String getPackageName(TypeMirror extensionJAXBRootClass) {
        String rootClassName = extensionJAXBRootClass.toString();
        return rootClassName.substring(0, rootClassName.lastIndexOf("."));
    }

    private void registerExtensionAnnotation(Extension extensionAnnotation, Element element) {
        init(extensionAnnotation, element);
        try {
            Object extension = readExtensionFile(extensionAnnotation.extensionFile());
            addExtensionConfigurations(extension);
        } catch (JAXBException | IOException ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage(), element);
        }
    }

    private Object readExtensionFile(String extensionFile) throws JAXBException, IOException {
        JAXBContext jaxbContext = createJAXBContext();
        FileObject extensionFileObject = processingEnv.getFiler().getResource(StandardLocation.CLASS_PATH, "", extensionFile);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        try (InputStream is = extensionFileObject.openInputStream()) {
            return unmarshaller.unmarshal(is);
        }
    }

    private TypeMirror getExtensionJAXBRootClass(Extension extensionAnnotation) {
        TypeMirror contentType = null;
        try {
            extensionAnnotation.extensionJAXBRootClass();
        } catch (MirroredTypeException mte) {
            contentType = mte.getTypeMirror();
        }
        return contentType;
    }

}
