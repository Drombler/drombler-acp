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
import org.richclientplatform.core.action.jaxb.ActionType;
import org.richclientplatform.core.action.jaxb.ActionsType;
import org.richclientplatform.core.application.AbstractApplicationAnnotationProcessor;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes("org.richclientplatform.core.action.Action")
public class ActionAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private ActionsType actions;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                Action actionAnnotation = element.getAnnotation(Action.class);
                if (actionAnnotation != null) {
                    if (actions == null) {
                        actions = new ActionsType();
                        addExtensionConfigurations(actions);
                        addJAXBRootClasses(ActionsType.class);
                    }
                    addOriginatingElements(element); // TODO: needed?

                    ActionType action = new ActionType();
                    action.setId(StringUtils.stripToNull(actionAnnotation.id()));
                    action.setCategory(StringUtils.stripToNull(actionAnnotation.category()));
                    action.setDisplayName(StringUtils.stripToNull(actionAnnotation.displayName()));
                    action.setAccelerator(StringUtils.stripToNull(actionAnnotation.accelerator()));
                    action.setIcon(StringUtils.stripToNull(actionAnnotation.icon()));
                    action.setListenerClass(element.asType().toString());
                    actions.getAction().add(action);
                }
            }
        }

        return false;
    }
}
