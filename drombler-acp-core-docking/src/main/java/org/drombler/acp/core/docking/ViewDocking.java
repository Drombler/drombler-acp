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
package org.drombler.acp.core.docking;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ResourceBundle;
import org.drombler.commons.client.util.ResourceBundleUtils;

/**
 * Registers an view dockable.<br>
 * <br>
 * Views are singleton dockables.
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ViewDocking {

//    String id();
    /**
     * The Docking Area ID to which this view should be docked.
     *
     * @return the Docking Area ID to which this view should be docked
     * @see DockingArea#id()
     */
    String areaId();

    /**
     * The position to order the views in a Docking Area. It's a best practice to leave out some positions between entries to allow other bundles to register entries between some existing ones.
     *
     * @return the position to order the views in a Docking Area
     */
    int position();

//    boolean singleton() default true;
    /**
     * The text to be displayed, e.g. as the text for tabs or menu items. If the value starts with '%' the rest of the value is interpreted as a property key and the value gets looked-up in the
     * Bundle.properties file (or a locale specific derivation of this file), which has to be in the same package as the annotated view.
     *
     * @return the text to be displayed for this view
     */
    String displayName();

    /**
     * The accelerator key combination to open this view.
     *
     * @return the accelerator key combination to open this view
     */
    String accelerator() default "";

    /**
     * The icon name pattern to resolve the icons to be used for this view.
     *
     * Note that this only specifies the name pattern. Drombler ACP looks for &lt;icon-base-name&gt;16.&lt;icon-extension&gt; for tabs and menu items (expected to be 16x16 pixels) and
     * &lt;icon-base-name&gt;24.&lt;icon-extension&gt; for toolbar buttons (expected to be 24x24 pixels). So if icon is &quot;test.png&quot;, Drombler ACP would look for test16.png (for tabs and menu
     * items) and test24.png (for toolbar buttons).
     *
     * @return the icon name pattern
     */
    String icon() default "";

    /**
     * The initial docking state. Currently this has no effect, but might be supported in a future version.
     *
     * @return the initial docking state
     */
    DockingState state() default DockingState.DOCKED;

    /**
     * The menu entry to open this view.
     *
     * @return the menu entry to open this view
     */
    WindowMenuEntry menuEntry();

    /**
     * The {@link ResourceBundle} base name.<br>
     * <br>
     * <ul>
     * <li>If resourceBundleBaseName is empty, a {@link ResourceBundle} will be looked up which is in the same package as the annotated view and has a base name equal to the simple name of the
     * annotated view.</li>
     * <li>If resourceBundleBaseName equals 'Bundle', the package {@link ResourceBundle} gets looked up.</li>
     * <li>Else a {@link ResourceBundle} for the resourceBundleBaseName gets looked up using the same {@link ClassLoader} as the annotated view.</li>
     * </ul>
     *
     * @return the base name of the ResourceBundle, 'Bundle' (for the package ResourceBundle) or empty (for the class ResourceBundle).
     * @see ResourceBundleUtils#PACKAGE_RESOURCE_BUNDLE_BASE_NAME
     * @see ResourceBundleUtils#getClassResourceStringPrefixed(java.lang.Class, java.lang.String)
     */
    String resourceBundleBaseName() default "";
}
