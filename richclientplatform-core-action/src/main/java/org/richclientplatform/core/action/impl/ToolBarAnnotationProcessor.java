/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.impl;

import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.apache.commons.lang.StringUtils;
import org.richclientplatform.core.action.Action;
import org.richclientplatform.core.action.ToggleAction;
import org.richclientplatform.core.action.ToolBar;
import org.richclientplatform.core.action.ToolBarEntry;
import org.richclientplatform.core.action.ToolBarToggleEntry;
import org.richclientplatform.core.action.ToolBars;
import org.richclientplatform.core.action.jaxb.ToolBarEntryType;
import org.richclientplatform.core.action.jaxb.ToolBarToggleEntryType;
import org.richclientplatform.core.action.jaxb.ToolBarType;
import org.richclientplatform.core.action.jaxb.ToolBarsType;
import org.richclientplatform.core.application.AbstractApplicationAnnotationProcessor;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({"org.richclientplatform.core.action.ToolBars", "org.richclientplatform.core.action.ToolBar",
    "org.richclientplatform.core.action.ToolBarEntry"})
public class ToolBarAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private ToolBarsType toolBars;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ToolBars.class)) {
            ToolBars toolBarsAnnotation = element.getAnnotation(ToolBars.class);
            if (toolBarsAnnotation != null) {
                for (ToolBar toolBarAnnotation : toolBarsAnnotation.value()) {
                    registerToolBar(toolBarAnnotation, element);
                }
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(ToolBar.class)) {
            ToolBar toolBarAnnotation = element.getAnnotation(ToolBar.class);
            if (toolBarAnnotation != null) {
                registerToolBar(toolBarAnnotation, element);
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(ToolBarEntry.class)) {
            ToolBarEntry toolBarEntryAnnotation = element.getAnnotation(ToolBarEntry.class);
            if (toolBarEntryAnnotation != null) {
                Action actionAnnotation = element.getAnnotation(Action.class);
                registerToolBarEntry(toolBarEntryAnnotation, actionAnnotation, element);
            }
        }


        for (Element element : roundEnv.getElementsAnnotatedWith(ToolBarToggleEntry.class)) {
            ToolBarToggleEntry toolBarEntryAnnotation = element.getAnnotation(ToolBarToggleEntry.class);
            if (toolBarEntryAnnotation != null) {
                ToggleAction actionAnnotation = element.getAnnotation(ToggleAction.class);
                registerToolBarToggleEntry(toolBarEntryAnnotation, actionAnnotation, element);
            }
        }

        return false;
    }

    private void registerToolBar(ToolBar toolBarAnnotation, Element element) {
        init(element);

        ToolBarType toolBar = new ToolBarType();
        toolBar.setId(StringUtils.stripToNull(toolBarAnnotation.id()));
        toolBar.setDisplayName(StringUtils.stripToNull(toolBarAnnotation.displayName()));
        toolBar.setPosition(toolBarAnnotation.position());
        toolBar.setVisible(toolBarAnnotation.visible());
        toolBar.setPackage(element.asType().toString());
        toolBars.getToolBar().add(toolBar);
    }

    private void init(Element element) {
        if (toolBars == null) {
            toolBars = new ToolBarsType();
            addExtensionConfigurations(toolBars);
            addJAXBRootClasses(ToolBarsType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

    private void registerToolBarEntry(ToolBarEntry toolBarEntryAnnotation, Action actionAnnotation, Element element) {
        init(element);

        ToolBarEntryType toolBarEntry = new ToolBarEntryType();
        String actionAnnotationActionId = actionAnnotation != null ? actionAnnotation.id() : null;
        configureToolBarEntry(toolBarEntry, actionAnnotationActionId, toolBarEntryAnnotation.actionId(),
                toolBarEntryAnnotation.toolBarId(), toolBarEntryAnnotation.position());
        toolBars.getToolBarEntry().add(toolBarEntry);
    }

    private void registerToolBarToggleEntry(ToolBarToggleEntry toolBarEntryAnnotation, ToggleAction actionAnnotation, Element element) {
        ToolBarToggleEntryType toolBarToggleEntry = new ToolBarToggleEntryType();
        String actionAnnotationActionId = actionAnnotation != null ? actionAnnotation.id() : null;
        configureToolBarEntry(toolBarToggleEntry, actionAnnotationActionId, toolBarEntryAnnotation.actionId(),
                toolBarEntryAnnotation.toolBarId(), toolBarEntryAnnotation.position());
        toolBarToggleEntry.setToggleGroupId(StringUtils.stripToNull(toolBarEntryAnnotation.toggleGroupId()));
        toolBars.getToolBarToggleEntry().add(toolBarToggleEntry);
    }

    private void configureToolBarEntry(ToolBarEntryType toolBarEntry, String actionAnnotationActionId, String actionId, String toolBarId, int position) {
        actionId = StringUtils.stripToNull(actionId);
        if (actionId == null && actionAnnotationActionId != null) {
            actionId = StringUtils.stripToNull(actionAnnotationActionId);
        }
        toolBarEntry.setActionId(actionId);
        toolBarEntry.setToolBarId(StringUtils.stripToNull(toolBarId));
        toolBarEntry.setPosition(position);
    }
}
