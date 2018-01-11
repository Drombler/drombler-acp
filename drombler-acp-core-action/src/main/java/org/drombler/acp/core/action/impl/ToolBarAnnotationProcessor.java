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
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.ToggleAction;
import org.drombler.acp.core.action.ToolBar;
import org.drombler.acp.core.action.ToolBarEntry;
import org.drombler.acp.core.action.ToolBarToggleEntry;
import org.drombler.acp.core.action.ToolBars;
import org.drombler.acp.core.action.jaxb.ToolBarEntryType;
import org.drombler.acp.core.action.jaxb.ToolBarToggleEntryType;
import org.drombler.acp.core.action.jaxb.ToolBarType;
import org.drombler.acp.core.action.jaxb.ToolBarsType;
import org.drombler.acp.core.application.processing.AbstractApplicationAnnotationProcessor;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({"org.drombler.acp.core.action.ToolBars", "org.drombler.acp.core.action.ToolBar",
    "org.drombler.acp.core.action.ToolBarEntry", "org.drombler.acp.core.action.ToolBarToggleEntry"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ToolBarAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private ToolBarsType toolBars;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(ToolBars.class).forEach(element -> {
            ToolBars toolBarsAnnotation = element.getAnnotation(ToolBars.class);
            if (toolBarsAnnotation != null) {
                for (ToolBar toolBarAnnotation : toolBarsAnnotation.value()) {
                    registerToolBar(toolBarAnnotation, element);
                }
            }
        });

        roundEnv.getElementsAnnotatedWith(ToolBar.class).forEach(element -> {
            ToolBar toolBarAnnotation = element.getAnnotation(ToolBar.class);
            if (toolBarAnnotation != null) {
                registerToolBar(toolBarAnnotation, element);
            }
        });

        roundEnv.getElementsAnnotatedWith(ToolBarEntry.class).forEach(element -> {
            ToolBarEntry toolBarEntryAnnotation = element.getAnnotation(ToolBarEntry.class);
            if (toolBarEntryAnnotation != null) {
                Action actionAnnotation = element.getAnnotation(Action.class);
                registerToolBarEntry(toolBarEntryAnnotation, actionAnnotation, element);
            }
        });

        roundEnv.getElementsAnnotatedWith(ToolBarToggleEntry.class).forEach(element -> {
            ToolBarToggleEntry toolBarEntryAnnotation = element.getAnnotation(ToolBarToggleEntry.class);
            if (toolBarEntryAnnotation != null) {
                ToggleAction actionAnnotation = element.getAnnotation(ToggleAction.class);
                registerToolBarToggleEntry(toolBarEntryAnnotation, actionAnnotation, element);
            }
        });

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
        init(element);

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
