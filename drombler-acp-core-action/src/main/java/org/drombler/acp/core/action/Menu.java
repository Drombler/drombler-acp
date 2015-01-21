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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation registers menus and sub-menus.
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
@Repeatable(Menus.class)
public @interface Menu {

    /**
     * The ID of the menu to be registered. This ID can be used in the path property of menus and menu items.
     *
     * @return The ID of the menu to be registered
     * @see #path()
     * @see MenuEntry#path()
     */
    String id();

    /**
     * The text of the menu. If the value starts with '%' the rest of the value is interpreted as a property key and the
     * value gets looked-up in the Bundle.properties file (or a locale specific derivation of this file), which has to
     * be in the same package.
     *
     * @return the text of the menu
     */
    String displayName();

    /**
     * A slash '/' delimited path of Menu IDs. If it is omitted, then the menu will be registered directly in the menu
     * bar.
     *
     * @return a slash '/' delimited path of Menu IDs
     */
    String path() default "";

    /**
     * The position to order the menus in a parent menu/ menu bar. It's a best practice to leave out some positions
     * between entries to allow other bundles to register entries between some existing ones.
     *
     * @return the position to order the menus in a parent menu/ menu bar
     */
    int position();
}
