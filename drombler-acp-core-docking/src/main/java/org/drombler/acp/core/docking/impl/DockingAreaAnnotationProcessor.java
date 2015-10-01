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

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.application.AbstractApplicationAnnotationProcessor;
import org.drombler.acp.core.docking.DockingArea;
import org.drombler.acp.core.docking.DockingAreas;
import org.drombler.acp.core.docking.jaxb.DockingAreaKindType;
import org.drombler.acp.core.docking.jaxb.DockingAreaPathsType;
import org.drombler.acp.core.docking.jaxb.DockingAreaType;
import org.drombler.acp.core.docking.jaxb.DockingAreasType;
import org.drombler.acp.core.docking.jaxb.LayoutConstraintsType;
import org.drombler.commons.docking.DockingAreaKind;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({"org.drombler.acp.core.docking.DockingAreas",
    "org.drombler.acp.core.docking.DockingArea"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DockingAreaAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    //TODO: not safe as changes to DockingAreaKind are missed at compile time. 
    // Possible solutions:
    // - Consider to generate a part of the XSD from DockingAreaKind and omit DockingAreaKindType
    // - Add a method toJaxb() to DockingAreaKind -> clutters the interface of the class (implementationg detail leaking in interface)
    // - Use a unit test to test if the static block throws an IllegalStateException (current solution) 
    //
    // Note: There is a similar issue when reading and handling the XML (changes to DockingAreaKindType are missed at compile time)
    private static final Map<DockingAreaKind, DockingAreaKindType> DOCKING_AREA_KINDS = new EnumMap<>(
            DockingAreaKind.class);

    static {
        DOCKING_AREA_KINDS.put(DockingAreaKind.VIEW, DockingAreaKindType.VIEW);
        DOCKING_AREA_KINDS.put(DockingAreaKind.EDITOR, DockingAreaKindType.EDITOR);
        for (DockingAreaKind dockingAreaKind : DockingAreaKind.values()) {
            if (!DOCKING_AREA_KINDS.containsKey(dockingAreaKind)) {
                throw new IllegalStateException("No mapping for: " + dockingAreaKind);
            }
        }
    }
    private DockingAreasType dockingAreas;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(DockingAreas.class)) {
            DockingAreas dockingAreasAnnotation = element.getAnnotation(DockingAreas.class);
            if (dockingAreasAnnotation != null) {
                for (DockingArea dockingAreaAnnotation : dockingAreasAnnotation.value()) {
                    registerDockingArea(dockingAreaAnnotation, element);
                }
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(DockingArea.class)) {
            DockingArea dockingAreaAnnotation = element.getAnnotation(DockingArea.class);
            if (dockingAreaAnnotation != null) {
                registerDockingArea(dockingAreaAnnotation, element);
            }
        }


        return false;
    }

    private void registerDockingArea(DockingArea dockingAreaAnnotation, Element element) {
        init(element);

        DockingAreaType dockingArea = new DockingAreaType();
        dockingArea.setId(StringUtils.stripToNull(dockingAreaAnnotation.id()));
        dockingArea.setKind(DOCKING_AREA_KINDS.get(dockingAreaAnnotation.kind()));
        dockingArea.setPosition(dockingAreaAnnotation.position());
        dockingArea.setPermanent(dockingAreaAnnotation.permanent());
        DockingAreaPathsType paths = new DockingAreaPathsType();
        for (int path : dockingAreaAnnotation.path()) {
            paths.getPath().add(path);
        }
        dockingArea.setPaths(paths);

        LayoutConstraintsType layoutConstraints = new LayoutConstraintsType();
        layoutConstraints.setPrefWidth(dockingAreaAnnotation.layoutConstraints().prefWidth());
        layoutConstraints.setPrefHeight(dockingAreaAnnotation.layoutConstraints().prefHeight());
        dockingArea.setLayoutConstraints(layoutConstraints);

        dockingAreas.getDockingArea().add(dockingArea);
    }

    private void init(Element element) {
        if (dockingAreas == null) {
            dockingAreas = new DockingAreasType();
            addExtensionConfigurations(dockingAreas);
            addJAXBRootClasses(DockingAreasType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }
}
