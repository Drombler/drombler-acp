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
package org.drombler.acp.core.settings;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The containing annotation type for the repeatable annotation {@link SettingsCategory}.
 *
 * Note: Since {@link SettingsCategory} is a {@link Repeatable} annotation, this containing annotation type is usually not needed to
 * be declared explicitly.
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PACKAGE, ElementType.TYPE})
public @interface SettingsCategories {

    /**
     * An array of the repeatable annotation {@link SettingsCategory}.
     *
     * @return an array of the repeatable annotation {@link SettingsCategory}
     */
    SettingsCategory[] value();
}