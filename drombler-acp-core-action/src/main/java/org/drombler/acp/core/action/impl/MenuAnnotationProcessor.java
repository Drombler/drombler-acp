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
import org.drombler.acp.core.action.Menu;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.action.Menus;
import org.drombler.acp.core.action.ToggleAction;
import org.drombler.acp.core.action.ToggleMenuEntry;
import org.drombler.acp.core.action.jaxb.MenuEntryType;
import org.drombler.acp.core.action.jaxb.MenuType;
import org.drombler.acp.core.action.jaxb.MenusType;
import org.drombler.acp.core.action.jaxb.ToggleMenuEntryType;
import org.drombler.acp.core.application.AbstractApplicationAnnotationProcessor;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({
    "org.drombler.acp.core.action.Menus",
    "org.drombler.acp.core.action.Menu",
    "org.drombler.acp.core.action.MenuEntry",
    "org.drombler.acp.core.action.ToggleMenuEntry"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class MenuAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private MenusType menus;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Menus.class)) {
            Menus menusAnnotation = element.getAnnotation(Menus.class);
            if (menusAnnotation != null) {
                for (Menu menuAnnotation : menusAnnotation.value()) {
                    registerMenu(menuAnnotation, element);
                }
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Menu.class)) {
            Menu menuAnnotation = element.getAnnotation(Menu.class);
            if (menuAnnotation != null) {
                registerMenu(menuAnnotation, element);
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(MenuEntry.class)) {
            MenuEntry menuEntryAnnotation = element.getAnnotation(MenuEntry.class);
            if (menuEntryAnnotation != null) {
                Action actionAnnotation = element.getAnnotation(Action.class);
                registerMenuEntry(menuEntryAnnotation, actionAnnotation, element);
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(ToggleMenuEntry.class)) {
            ToggleMenuEntry toggleMenuEntryAnnotation = element.getAnnotation(ToggleMenuEntry.class);
            if (toggleMenuEntryAnnotation != null) {
                ToggleAction actionAnnotation = element.getAnnotation(ToggleAction.class);
                registerToggleMenuEntry(toggleMenuEntryAnnotation, actionAnnotation, element);
            }
        }


        return false;
    }

    private void registerMenu(Menu menuAnnotation, Element element) {
        init(element);

        MenuType menu = new MenuType();
        menu.setId(StringUtils.stripToNull(menuAnnotation.id()));
        menu.setDisplayName(StringUtils.stripToNull(menuAnnotation.displayName()));
        menu.setPosition(menuAnnotation.position());
        menu.setPath(StringUtils.stripToNull(menuAnnotation.path()));
        menu.setPackage(element.asType().toString());
        menus.getMenu().add(menu);
    }

    private void init(Element element) {
        if (menus == null) {
            menus = new MenusType();
            addExtensionConfigurations(menus);
            addJAXBRootClasses(MenusType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

    private void registerMenuEntry(MenuEntry menuEntryAnnotation, Action actionAnnotation, Element element) {
        init(element);

        MenuEntryType menuEntry = new MenuEntryType();
        String actionAnnotationActionId = actionAnnotation != null ? actionAnnotation.id() : null;
        configureMenuEntry(menuEntry, actionAnnotationActionId, menuEntryAnnotation.actionId(),
                menuEntryAnnotation.position(), menuEntryAnnotation.path());
        menus.getMenuEntry().add(menuEntry);
    }

    private void registerToggleMenuEntry(ToggleMenuEntry toggleMenuEntryAnnotation, ToggleAction actionAnnotation, Element element) {
        init(element);

        ToggleMenuEntryType menuEntry = new ToggleMenuEntryType();
        String actionAnnotationActionId = actionAnnotation != null ? actionAnnotation.id() : null;
        configureMenuEntry(menuEntry, actionAnnotationActionId, toggleMenuEntryAnnotation.actionId(),
                toggleMenuEntryAnnotation.position(), toggleMenuEntryAnnotation.path());
        menuEntry.setToggleGroupId(StringUtils.stripToNull(toggleMenuEntryAnnotation.toggleGroupId()));
        menus.getToggleMenuEntry().add(menuEntry);
    }

    private void configureMenuEntry(MenuEntryType menuEntry, String actionAnnotationActionId, String actionId, int position, String path) {
        actionId = StringUtils.stripToNull(actionId);
        if (actionId == null && actionAnnotationActionId != null) {
            actionId = StringUtils.stripToNull(actionAnnotationActionId);
        }
        menuEntry.setActionId(actionId);
        menuEntry.setPosition(position);
        menuEntry.setPath(StringUtils.stripToNull(path));
    }
}
