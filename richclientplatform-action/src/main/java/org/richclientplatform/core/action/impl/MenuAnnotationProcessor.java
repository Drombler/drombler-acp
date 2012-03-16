/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.apache.commons.lang.StringUtils;
import org.richclientplatform.core.action.Menu;
import org.richclientplatform.core.action.jaxb.MenuType;
import org.richclientplatform.core.action.jaxb.Menus;
import org.richclientplatform.core.application.AbstractApplicationAnnotationProcessor;

/**
 *
 * @author puce
 */
@SupportedAnnotationTypes("org.richclientplatform.core.action.Menu")
public class MenuAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private Menus menus;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                Menu menuAnnotation = element.getAnnotation(Menu.class);
                if (menuAnnotation != null) {
                    if (menus == null) {
                        menus =  new Menus();
                        addExtensionConfigurations(menus);
                        addJAXBRootClasses(Menus.class);
                    }
                    addOriginatingElements(element); // TODO: needed?

                    MenuType menu = new MenuType();
                    menu.setId(StringUtils.stripToNull(menuAnnotation.id()));
                    menu.setDisplayName(StringUtils.stripToNull(menuAnnotation.displayName()));
                    menu.setPosition(menuAnnotation.position());
                    menu.setPath(StringUtils.stripToNull(menuAnnotation.path()));
                    menus.getMenus().add(menu);
                }
            }
        }

        return false;
    }
}
