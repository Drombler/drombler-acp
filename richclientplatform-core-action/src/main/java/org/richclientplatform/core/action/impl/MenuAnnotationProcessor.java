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
import org.richclientplatform.core.action.CheckMenuEntry;
import org.richclientplatform.core.action.Menu;
import org.richclientplatform.core.action.MenuEntry;
import org.richclientplatform.core.action.Menus;
import org.richclientplatform.core.action.RadioMenuEntry;
import org.richclientplatform.core.action.jaxb.CheckMenuEntryType;
import org.richclientplatform.core.action.jaxb.MenuEntryType;
import org.richclientplatform.core.action.jaxb.MenuType;
import org.richclientplatform.core.action.jaxb.MenusType;
import org.richclientplatform.core.action.jaxb.RadioMenuEntryType;
import org.richclientplatform.core.application.AbstractApplicationAnnotationProcessor;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({
    "org.richclientplatform.core.action.Menus",
    "org.richclientplatform.core.action.Menu",
    "org.richclientplatform.core.action.MenuEntry",
    "org.richclientplatform.core.action.CheckMenuEntry",
    "org.richclientplatform.core.action.RadioMenuEntry"})
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

        for (Element element : roundEnv.getElementsAnnotatedWith(CheckMenuEntry.class)) {
            CheckMenuEntry checkMenuEntryAnnotation = element.getAnnotation(CheckMenuEntry.class);
            if (checkMenuEntryAnnotation != null) {
                Action actionAnnotation = element.getAnnotation(Action.class);
                registerCheckMenuEntry(checkMenuEntryAnnotation, actionAnnotation, element);
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(RadioMenuEntry.class)) {
            RadioMenuEntry radioMenuEntryAnnotation = element.getAnnotation(RadioMenuEntry.class);
            if (radioMenuEntryAnnotation != null) {
                Action actionAnnotation = element.getAnnotation(Action.class);
                registerRadioMenuEntry(radioMenuEntryAnnotation, actionAnnotation, element);
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
        configureMenuEntry(menuEntry, actionAnnotation, menuEntryAnnotation.actionId(), menuEntryAnnotation.position(),
                menuEntryAnnotation.path());
        menus.getMenuEntry().add(menuEntry);
    }

    private void registerCheckMenuEntry(CheckMenuEntry checkMenuEntryAnnotation, Action actionAnnotation, Element element) {
        init(element);

        CheckMenuEntryType menuEntry = new CheckMenuEntryType();
        configureMenuEntry(menuEntry, actionAnnotation, checkMenuEntryAnnotation.actionId(),
                checkMenuEntryAnnotation.position(),
                checkMenuEntryAnnotation.path());
        menus.getCheckMenuEntry().add(menuEntry);
    }

    private void registerRadioMenuEntry(RadioMenuEntry radioMenuEntryAnnotation, Action actionAnnotation, Element element) {
        init(element);

        RadioMenuEntryType menuEntry = new RadioMenuEntryType();
        configureMenuEntry(menuEntry, actionAnnotation, radioMenuEntryAnnotation.actionId(),
                radioMenuEntryAnnotation.position(),
                radioMenuEntryAnnotation.path());
        menuEntry.setToggleGroupId(StringUtils.stripToNull(radioMenuEntryAnnotation.toggleGroupId()));
        menus.getRadioMenuEntry().add(menuEntry);
    }

    private void configureMenuEntry(MenuEntryType menuEntry, Action actionAnnotation, String actionId, int position, String path) {
        actionId = StringUtils.stripToNull(actionId);
        if (actionId == null && actionAnnotation != null) {
            actionId = StringUtils.stripToNull(actionAnnotation.id());
        }
        menuEntry.setActionId(actionId);
        menuEntry.setPosition(position);
        menuEntry.setPath(StringUtils.stripToNull(path));
    }
}
