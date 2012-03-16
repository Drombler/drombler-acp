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
import org.richclientplatform.core.action.Menu;
import org.richclientplatform.core.action.Menus;
import org.richclientplatform.core.action.jaxb.MenuType;
import org.richclientplatform.core.action.jaxb.MenusType;
import org.richclientplatform.core.application.AbstractApplicationAnnotationProcessor;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes({"org.richclientplatform.core.action.Menus", "org.richclientplatform.core.action.Menu"})
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


        return false;
    }

    private void registerMenu(Menu menuAnnotation, Element element) {
        if (menus == null) {
            menus = new MenusType();
            addExtensionConfigurations(menus);
            addJAXBRootClasses(MenusType.class);
        }
        addOriginatingElements(element); // TODO: needed?

        MenuType menu = new MenuType();
        menu.setId(StringUtils.stripToNull(menuAnnotation.id()));
        menu.setDisplayName(StringUtils.stripToNull(menuAnnotation.displayName()));
        menu.setPosition(menuAnnotation.position());
        menu.setPath(StringUtils.stripToNull(menuAnnotation.path()));
        menus.getMenu().add(menu);
    }
}
