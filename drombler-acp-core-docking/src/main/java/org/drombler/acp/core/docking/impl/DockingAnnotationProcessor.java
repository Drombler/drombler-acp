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
package org.drombler.acp.core.docking.impl;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.application.processing.AbstractApplicationAnnotationProcessor;
import org.drombler.acp.core.docking.DockingState;
import org.drombler.acp.core.docking.EditorDocking;
import org.drombler.acp.core.docking.ViewDocking;
import org.drombler.acp.core.docking.jaxb.AbstractDockingType;
import org.drombler.acp.core.docking.jaxb.DockingStateType;
import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.acp.core.docking.jaxb.EditorDockingType;
import org.drombler.acp.core.docking.jaxb.ViewDockingType;
import org.drombler.acp.core.docking.jaxb.WindowMenuEntryType;
import org.softsmithy.lib.lang.model.type.ModelTypeUtils;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({"org.drombler.acp.core.docking.ViewDocking", "org.drombler.acp.core.docking.EditorDocking"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class DockingAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    //TODO: not safe as changes to DockingAreaKind are missed at compile time. 
    // Possible solutions:
    // - Consider to generate a part of the XSD from DockingAreaKind and omit DockingAreaKindType
    // - Add a method toJaxb() to DockingAreaKind -> clutters the interface of the class (implementationg detail leaking in interface)
    // - Use a unit test to test if the static block throws an IllegalStateException (current solution) 
    //
    // Note: There is a similar issue when reading and handling the XML (changes to DockingAreaKindType are missed at compile time)
    private static final Map<DockingState, DockingStateType> DOCKING_STATES = new EnumMap<>(
            DockingState.class);

    static {
        DOCKING_STATES.put(DockingState.DOCKED, DockingStateType.DOCKED);
        DOCKING_STATES.put(DockingState.FLOATING, DockingStateType.FLOATING);
        DOCKING_STATES.put(DockingState.HIDDEN, DockingStateType.HIDDEN);
        DOCKING_STATES.put(DockingState.MAXIMIZED, DockingStateType.MAXIMIZED);
        DOCKING_STATES.put(DockingState.MINIMIZED, DockingStateType.MINIMIZED);
        for (DockingState dockingState : DockingState.values()) {
            if (!DOCKING_STATES.containsKey(dockingState)) {
                throw new IllegalStateException("No mapping for: " + dockingState);
            }
        }
    }
    private DockingsType dockings;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(ViewDocking.class).forEach(element -> {
            ViewDocking dockingAnnotation = element.getAnnotation(ViewDocking.class);
            if (dockingAnnotation != null) {
                registerViewDocking(dockingAnnotation, element);
            }
        });

        roundEnv.getElementsAnnotatedWith(EditorDocking.class).forEach(element -> {
            EditorDocking dockingAnnotation = element.getAnnotation(EditorDocking.class);
            if (dockingAnnotation != null) {
                registerEditorDocking(dockingAnnotation, element);
            }
        });

        return false;
    }

    private void registerEditorDocking(EditorDocking dockingAnnotation, Element element) {
        TypeMirror contentType = ModelTypeUtils.getTypeMirror(dockingAnnotation::contentType);
        if (contentType != null) {
            if (ElementFilter.typesIn(Collections.singletonList(element)).stream()
                    .anyMatch(typeElement -> checkForContentTypeConstructor(typeElement, contentType))) {
                registerEditorDockingOnly(dockingAnnotation, element);
            } else {
                // Couldn't find any matching constructor
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR, "Missing a public constructor with a single parameter of type: " + contentType,
                        element);
            }
        } else {
            // should not happen here
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR, "Couldn't retrieve the contentType information.",
                    element);
        }
    }

    private boolean checkForContentTypeConstructor(TypeElement type, TypeMirror contentType) {
        return ElementFilter.constructorsIn(type.getEnclosedElements()).stream().
                anyMatch(constructor -> isContentTypeConstructor(constructor, contentType));
    }

    private static boolean isContentTypeConstructor(ExecutableElement cons, TypeMirror contentType) {
        return cons.getModifiers().contains(Modifier.PUBLIC)
                && cons.getParameters().size() == 1
                && cons.getParameters().get(0).asType().equals(contentType);
    }

    private void registerViewDocking(ViewDocking dockingAnnotation, Element element) {
        init(element);

        ViewDockingType docking = new ViewDockingType();
        configureDocking(docking, element);

        docking.setAreaId(StringUtils.stripToNull(dockingAnnotation.areaId()));
        docking.setIcon(StringUtils.stripToNull(dockingAnnotation.icon()));
        docking.setState(DOCKING_STATES.get(dockingAnnotation.state()));
        docking.setPosition(dockingAnnotation.position());
//        docking.setSingleton(dockingAnnotation.singleton());
        docking.setDisplayName(StringUtils.stripToNull(dockingAnnotation.displayName()));
        docking.setAccelerator(StringUtils.stripToNull(dockingAnnotation.accelerator()));
        docking.setResourceBundleBaseName(StringUtils.stripToNull(dockingAnnotation.resourceBundleBaseName()));

        WindowMenuEntryType windowMenuEntry = new WindowMenuEntryType();
        windowMenuEntry.setPosition(dockingAnnotation.menuEntry().position());
        windowMenuEntry.setPath(StringUtils.stripToNull(dockingAnnotation.menuEntry().path()));
        docking.setMenuEntry(windowMenuEntry);

        dockings.getViewDocking().add(docking);
    }

    private void configureDocking(AbstractDockingType docking, Element element) {
        docking.setId(element.asType().toString());
        docking.setDockableClass(element.asType().toString());
    }

    private void init(Element element) {
        if (dockings == null) {
            dockings = new DockingsType();
            addExtensionConfiguration(dockings);
            addJAXBRootClass(DockingsType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

    private void registerEditorDockingOnly(EditorDocking dockingAnnotation, Element element) {
        init(element);

        EditorDockingType docking = new EditorDockingType();
        configureDocking(docking, element);
        try {
            dockingAnnotation.contentType();
        } catch (MirroredTypeException ex) {
            docking.setContentType(ex.getTypeMirror().toString());
        }
        dockings.getEditorDocking().add(docking);
    }
}
