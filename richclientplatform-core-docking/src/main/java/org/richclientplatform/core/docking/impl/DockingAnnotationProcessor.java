/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.impl;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.apache.commons.lang.StringUtils;
import org.richclientplatform.core.application.AbstractApplicationAnnotationProcessor;
import org.richclientplatform.core.docking.Docking;
import org.richclientplatform.core.docking.DockingState;
import org.richclientplatform.core.docking.jaxb.DockingStateType;
import org.richclientplatform.core.docking.jaxb.DockingType;
import org.richclientplatform.core.docking.jaxb.DockingsType;
import org.richclientplatform.core.docking.jaxb.WindowMenuEntryType;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes("org.richclientplatform.core.docking.Docking")
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
        for (Element element : roundEnv.getElementsAnnotatedWith(Docking.class)) {
            Docking dockingAnnotation = element.getAnnotation(Docking.class);
            if (dockingAnnotation != null) {
                registerDocking(dockingAnnotation, element);
            }
        }


        return false;
    }

    private void registerDocking(Docking dockingAnnotation, Element element) {
        init(element);

        DockingType docking = new DockingType();
        docking.setId(element.asType().toString());
        docking.setAreaId(StringUtils.stripToNull(dockingAnnotation.areaId()));
        docking.setPosition(dockingAnnotation.position());
//        docking.setSingleton(dockingAnnotation.singleton());
        docking.setDisplayName(StringUtils.stripToNull(dockingAnnotation.displayName()));
        docking.setAccelerator(StringUtils.stripToNull(dockingAnnotation.accelerator()));
        docking.setIcon(StringUtils.stripToNull(dockingAnnotation.icon()));
        docking.setState(DOCKING_STATES.get(dockingAnnotation.state()));
        docking.setDockableClass(element.asType().toString());
        
        WindowMenuEntryType windowMenuEntry = new WindowMenuEntryType();
        windowMenuEntry.setPosition(dockingAnnotation.menuEntry().position());
        windowMenuEntry.setPath(StringUtils.stripToNull(dockingAnnotation.menuEntry().path()));
        docking.setMenuEntry(windowMenuEntry);
        
        dockings.getDocking().add(docking);
    }

    private void init(Element element) {
        if (dockings == null) {
            dockings = new DockingsType();
            addExtensionConfigurations(dockings);
            addJAXBRootClasses(DockingsType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }
}
