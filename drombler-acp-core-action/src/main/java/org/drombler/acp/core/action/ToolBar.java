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
 * This annotation registers a tool bar.
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
@Repeatable(ToolBars.class)
public @interface ToolBar {

    /**
     * The id of this tool bar.
     *
     * An tool bar id can be referenced from other annotations such as {@link ToolBarEntry} and {@link ToolBarToggleEntry}.
     *
     * @return the id of this tool bar
     */
    String id();

    /**
     * The text to be displayed, e.g. as the text for toggle menu items. If the value starts with '%' the rest of the value is interpreted as a property key and the value gets looked-up in the
     * Bundle.properties file (or a locale specific derivation of this file), which has to be in the same package as the annotated package.
     *
     * @return the text to be displayed for this tool bar
     */
    String displayName();

    /**
     * The position to order the tool bars. It's a best practice to leave out some positions between entries to allow other bundles to register entries between some existing ones.
     *
     * @return the position to order the tool bars
     */
    int position();

    /**
     * Flag if this tool bar should be visible by default.
     *
     * @return true, if this tool bar should be visible by default, else false
     */
    boolean visible() default true;
}
