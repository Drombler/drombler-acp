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
 * This annotation registers a tool bar toggle entry.
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface ToolBarToggleEntry {

    /**
     * The id of the toggle action to be registered as a tool bar toggle entry. This property can be omitted, if there is a {@link ToggleAction} annotation on the same class.
     *
     * @return the id of the toggle action to be registered as a tool bar toggle entry
     * @see ToggleAction#id()
     */
    String actionId() default "";

    /**
     * The id of a tool bar to which this entry is registered.
     *
     * @return the id of a tool bar
     * @see ToolBar#id()
     */
    String toolBarId();

    /**
     * The id of a toggle group. Toggle groups are managed implicitly. All tool bar toggle entries with the same toggle group id are grouped in the same toggle group. If no group id is provided then
     * the tool bar toggle entry won't be part of a toggle group.
     *
     * @return the id of a toggle group
     */
    String toggleGroupId() default "";

    /**
     * The position to order the tool bar entries in a tool bar. It's a best practice to leave out some positions between entries to allow other bundles to register entries between some existing ones.
     *
     * @return the position to order the tool bar entries in a tool bar
     */
    int position();
}
