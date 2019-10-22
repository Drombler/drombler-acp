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
import org.drombler.commons.client.util.ResourceBundleUtils;

/**
 * This annotation registers an Action.<br>
 * <br>
 * Action can be used to configure and synchronize e.g. menu items and toolbar buttons.
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
     * &lt;icon-base-name&gt;16.&lt;icon-extension&gt; for menu items (expected to be 16x16 pixels) and
     * &lt;icon-base-name&gt;24.&lt;icon-extension&gt; for toolbar buttons (expected to be 24x24 pixels). So if icon is &quot;test.png&quot;, Drombler ACP would look for test16.png (for menu items) and
     * test24.png (for toolbar buttons).
     *
     * @return the icon name pattern
     */
    String icon() default "";

    /**
     * The {@link ResourceBundle} base name.<br>
     * <br>
     * <ul>
     * <li>If resourceBundleBaseName is empty, a {@link ResourceBundle} will be looked up which is in the same package as the annotated action class and has a base name equal to the simple name of the
     * annotated action class.</li>
     * <li>If resourceBundleBaseName equals 'Bundle', the package {@link ResourceBundle} gets looked up.</li>
     * <li>Else a {@link ResourceBundle} for the resourceBundleBaseName gets looked up using the same {@link ClassLoader} as the annotated action class.</li>
     * </ul>
     *
     * @return the base name of the ResourceBundle, 'Bundle' (for the package ResourceBundle) or empty (for the class ResourceBundle).
     * @see ResourceBundleUtils#PACKAGE_RESOURCE_BUNDLE_BASE_NAME
     * @see ResourceBundleUtils#getResourceBundle(java.lang.Class, java.lang.String, java.lang.String)
     */
    String resourceBundleBaseName() default "";
}
