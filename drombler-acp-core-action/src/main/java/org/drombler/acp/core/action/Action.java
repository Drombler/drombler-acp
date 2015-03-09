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
package org.drombler.acp.core.action;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation registers an Action.
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Action {

    /**
     * The id of this action.
     *
     * An action can be referenced from other annotations such as {@link MenuEntry} and {@link ToolBarEntry} by its id.
     *
     * @return the id of this action
     */
    String id();

    /**
     * The category is used to group actions (currently this has not effect, but might be used in future).
     *
     * @return the category of this action
     */
    String category();

    /**
     * The text to be displayed, e.g. as the text for menu items or the tooltip for toolbar buttons. If the value starts
     * with '%' the rest of the value is interpreted as a property key and the value gets looked-up in the
     * Bundle.properties file (or a locale specific derivation of this file), which has to be in the same package as the
     * annotated action.
     *
     * @return the text to be displayed for this action
     */
    String displayName();

    /**
     * The accelerator to be used for this action.
     *
     * @return the accelerator to be used for this action
     */
    String accelerator() default "";

    /**
     * The icon name pattern to resolve the icons to be used for this action.
     *
     * Note that this only specifies the name pattern. Drombler ACP looks for
     * <icon-base-name>16.<icon-extension> for menu items (expected to be 16x16 pixels) and
     * <icon-base-name>24.<icon-extension> for toolbar buttons (expected to be 24x24 pixels). So if icon is
     * &quot;test.png&quot;, Drombler ACP would look for test16.png (for menu items) and test24.png (for toolbar
     * buttons).
     *
     * @return the icon name pattern
     */
    String icon() default "";
}
