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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A window menu entry.
 *
 * @see ViewDocking#menuEntry()
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({})
public @interface WindowMenuEntry {

    /**
     * A slash '/' delimited path of menu IDs starting from the default location.
     *
     * @return a slash '/' delimited path of menu IDs starting from the default location.
     */
    // TODO: return a String array of menu IDs instead of a '/' delimited path?
    String path() default "";

    /**
     * The position to order the menu items in a menu. It's a best practice to leave out some positions between entries to allow other bundles to register entries between some existing ones.
     *
     * @return the position to order the menu items in a menu
     */
    int position();
}
