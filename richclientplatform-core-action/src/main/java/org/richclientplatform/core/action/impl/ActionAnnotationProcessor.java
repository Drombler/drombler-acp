/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.impl;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.apache.commons.lang.StringUtils;
import org.richclientplatform.core.action.Action;
import org.richclientplatform.core.action.ToggleAction;
import org.richclientplatform.core.action.jaxb.ActionType;
import org.richclientplatform.core.action.jaxb.ActionsType;
import org.richclientplatform.core.action.jaxb.ToggleActionType;
import org.richclientplatform.core.application.AbstractApplicationAnnotationProcessor;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({
    "org.richclientplatform.core.action.Action",
    "org.richclientplatform.core.action.ToggleAction"
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
