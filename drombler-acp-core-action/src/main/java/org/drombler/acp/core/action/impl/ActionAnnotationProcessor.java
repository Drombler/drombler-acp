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
package org.drombler.acp.core.action.impl;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.ToggleAction;
import org.drombler.acp.core.action.jaxb.ActionType;
import org.drombler.acp.core.action.jaxb.ActionsType;
import org.drombler.acp.core.action.jaxb.ToggleActionType;
import org.drombler.acp.core.application.AbstractApplicationAnnotationProcessor;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({
    "org.drombler.acp.core.action.Action",
    "org.drombler.acp.core.action.ToggleAction"
})
public class ActionAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private ActionsType actions;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Action.class)) {
            Action actionAnnotation = element.getAnnotation(Action.class);
            if (actionAnnotation != null) {
                registerAction(element, actionAnnotation);
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(ToggleAction.class)) {
            ToggleAction actionAnnotation = element.getAnnotation(ToggleAction.class);
            if (actionAnnotation != null) {
                registerToggleAction(element, actionAnnotation);
            }
        }

        return false;
    }

    private void init(Element element) {
        if (actions == null) {
            actions = new ActionsType();
            addExtensionConfigurations(actions);
            addJAXBRootClasses(ActionsType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

    private void registerAction(Element element, Action actionAnnotation) {
        init(element);

        ActionType action = new ActionType();
        configureAction(action, actionAnnotation.id(), actionAnnotation.category(), actionAnnotation.displayName(),
                actionAnnotation.accelerator(), actionAnnotation.icon(), element);
        actions.getAction().add(action);
    }

    private void registerToggleAction(Element element, ToggleAction actionAnnotation) {
        init(element);

        ToggleActionType action = new ToggleActionType();
        configureAction(action, actionAnnotation.id(), actionAnnotation.category(), actionAnnotation.displayName(),
                actionAnnotation.accelerator(), actionAnnotation.icon(), element);
        actions.getToggleAction().add(action);
    }

    private void configureAction(ActionType action, String id, String category, String displayName, String accelerator,
            String icon, Element element) {
        action.setId(StringUtils.stripToNull(id));
        action.setCategory(StringUtils.stripToNull(category));
        action.setDisplayName(StringUtils.stripToNull(displayName));
        action.setAccelerator(StringUtils.stripToNull(accelerator));
        action.setIcon(StringUtils.stripToNull(icon));
        action.setListenerClass(element.asType().toString());
    }
}
