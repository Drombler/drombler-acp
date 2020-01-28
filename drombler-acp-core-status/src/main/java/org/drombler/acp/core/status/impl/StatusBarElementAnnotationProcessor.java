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
package org.drombler.acp.core.status.impl;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.drombler.acp.core.application.processing.AbstractApplicationAnnotationProcessor;
import org.drombler.acp.core.status.StatusBarElement;
import org.drombler.acp.core.status.jaxb.HorizontalAlignmentType;
import org.drombler.acp.core.status.jaxb.StatusBarElementType;
import org.drombler.acp.core.status.jaxb.StatusBarElementsType;
import org.drombler.commons.client.geometry.HorizontalAlignment;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({"org.drombler.acp.core.status.StatusBarElement"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class StatusBarElementAnnotationProcessor extends AbstractApplicationAnnotationProcessor {
    //TODO: not safe as changes to HorizontalAlignment are missed at compile time. 
    // Possible solutions:
    // - Consider to generate a part of the XSD from HorizontalAlignment and omit HorizontalAlignmentType
    // - Add a method toJaxb() to HorizontalAlignment -> clutters the interface of the class (implementationg detail leaking in interface)
    // - Use a unit test to test if the static block throws an IllegalStateException (current solution) 
    //
    // Note: There is a similar issue when reading and handling the XML (changes to HorizontalAlignmentType are missed at compile time)
    private static final Map<HorizontalAlignment, HorizontalAlignmentType> HORIZONTAL_ALIGNMENT_MAP = new EnumMap<>(HorizontalAlignment.class);

    static {
        HORIZONTAL_ALIGNMENT_MAP.put(HorizontalAlignment.LEFT, HorizontalAlignmentType.LEFT);
        HORIZONTAL_ALIGNMENT_MAP.put(HorizontalAlignment.CENTER, HorizontalAlignmentType.CENTER);
        HORIZONTAL_ALIGNMENT_MAP.put(HorizontalAlignment.RIGHT, HorizontalAlignmentType.RIGHT);
        HORIZONTAL_ALIGNMENT_MAP.put(HorizontalAlignment.LEADING, HorizontalAlignmentType.LEADING);
        HORIZONTAL_ALIGNMENT_MAP.put(HorizontalAlignment.TRAILING, HorizontalAlignmentType.TRAILING);
        for (HorizontalAlignment horizontalAlignment : HorizontalAlignment.values()) {
            if (!HORIZONTAL_ALIGNMENT_MAP.containsKey(horizontalAlignment)) {
                throw new IllegalStateException("No mapping for: " + horizontalAlignment);
            }
        }
    }
    private StatusBarElementsType statusBarElements;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(StatusBarElement.class).forEach(element -> {
            StatusBarElement statusBarElementAnnotation = element.getAnnotation(StatusBarElement.class);
            if (statusBarElementAnnotation != null) {
                registerStatusBarElement(statusBarElementAnnotation, element);
            }
        });
        return false;
    }

    private void registerStatusBarElement(StatusBarElement statusBarElementAnnotation, Element element) {
        init(element);

        StatusBarElementType statusBarElement = new StatusBarElementType();
        statusBarElement.setHorizontalAlignment(HORIZONTAL_ALIGNMENT_MAP.get(statusBarElementAnnotation.horizontalAlignment()));
        statusBarElement.setPosition(statusBarElementAnnotation.position());
        statusBarElement.setStatusBarElementClass(element.asType().toString());
        statusBarElements.getStatusBarElement().add(statusBarElement);
    }

    private void init(Element element) {
        if (statusBarElements == null) {
            statusBarElements = new StatusBarElementsType();
            addExtensionConfiguration(statusBarElements);
            addJAXBRootClass(StatusBarElementsType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

}
