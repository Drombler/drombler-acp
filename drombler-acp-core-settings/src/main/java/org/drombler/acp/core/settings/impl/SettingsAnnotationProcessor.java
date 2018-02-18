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
package org.drombler.acp.core.settings.impl;

import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.application.processing.AbstractApplicationAnnotationProcessor;
import org.drombler.acp.core.settings.SettingsCategories;
import org.drombler.acp.core.settings.SettingsCategory;
import org.drombler.acp.core.settings.jaxb.AbstractSettingsCategoryType;
import org.drombler.acp.core.settings.jaxb.CustomSettingsCategoryType;
import org.drombler.acp.core.settings.jaxb.ParentOnlySettingsCategoryType;
import org.drombler.acp.core.settings.jaxb.SettingsType;

@SupportedAnnotationTypes({"org.drombler.acp.core.settings.SettingsCategory"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SettingsAnnotationProcessor extends AbstractApplicationAnnotationProcessor {

    private SettingsType settings;

    @Override
    protected boolean handleProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(SettingsCategories.class).forEach(element -> {
            SettingsCategories settingsCategoriesAnnotation = element.getAnnotation(SettingsCategories.class);
            if (settingsCategoriesAnnotation != null) {
                for (SettingsCategory settingsCategoryAnnotation : settingsCategoriesAnnotation.value()) {
                    registerSettingsCategory(settingsCategoryAnnotation, element);
                }
            }
        });

        roundEnv.getElementsAnnotatedWith(SettingsCategory.class).forEach(element -> {
            SettingsCategory settingsCategoryAnnotation = element.getAnnotation(SettingsCategory.class);
            if (settingsCategoryAnnotation != null) {
                registerSettingsCategory(settingsCategoryAnnotation, element);
            }
        });

        return false;
    }

    private void registerSettingsCategory(SettingsCategory settingsCategoryAnnotation, Element element) {
        init(element);
        if (element.asType().getKind() == TypeKind.PACKAGE) {
            ParentOnlySettingsCategoryType settingsCategory = new ParentOnlySettingsCategoryType();
            configureSettingsCategory(settingsCategory, settingsCategoryAnnotation);
            settingsCategory.setPackage(element.asType().toString());
            settings.getParentOnlySettingsCategory().add(settingsCategory);
        } else {
            CustomSettingsCategoryType settingsCategory = new CustomSettingsCategoryType();
            configureSettingsCategory(settingsCategory, settingsCategoryAnnotation);
            settingsCategory.setContentPaneClass(element.asType().toString());
            settings.getCustomSettingsCategory().add(settingsCategory);
        }
    }

    private void configureSettingsCategory(AbstractSettingsCategoryType settingsCategory, SettingsCategory settingsCategoryAnnotation) {
        settingsCategory.setId(StringUtils.stripToNull(settingsCategoryAnnotation.id()));
        settingsCategory.setDisplayName(StringUtils.stripToNull(settingsCategoryAnnotation.displayName()));
        settingsCategory.setDisplayDescription(StringUtils.stripToNull(settingsCategoryAnnotation.displayDescription()));
        settingsCategory.setPosition(settingsCategoryAnnotation.position());
        settingsCategory.setPath(StringUtils.stripToNull(settingsCategoryAnnotation.path()));
    }

    private void init(Element element) {
        if (settings == null) {
            settings = new SettingsType();
            addExtensionConfigurations(settings);
            addJAXBRootClasses(SettingsType.class);
        }
        addOriginatingElements(element); // TODO: needed?
    }

}
