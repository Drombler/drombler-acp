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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.drombler.commons.docking.DockingAreaDescriptor;
import org.drombler.commons.docking.DockingAreaKind;

/**
 * This annotation registers a Docking Area.
 *
 * @see DockingAreaDescriptor
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
@Repeatable(DockingAreas.class)
public @interface DockingArea {

    /**
     * The Docking Area ID.
     *
     * @return the Docking Area ID
     */
    String id();

    /**
     * The Docking Area kind.
     *
     * @return the Docking Area kind
     */
    DockingAreaKind kind() default DockingAreaKind.VIEW;

    /**
     * The preferred position of the Docking Area in the parent split pane. It's a best practice to leave out some positions between entries to allow other bundles to register entries between some
     * existing ones.
     *
     * @return the preferred position of the Docking Area in the parent split pane
     */
    int position();

    /**
     * The path positions of the parent split pane.
     *
     * @return the path positions of the parent split pane
     */
    int[] path();

    /**
     * Flag which indicates if the Docking Area is visible also when it's empty (permanently visible) or if the Docking Area is only visible when it's not empty.
     *
     * @return true if permanently visible, else false
     */
    boolean permanent() default false;

    /**
     * The layout constraints.
     *
     * @return the layout constraints
     */
    LayoutConstraints layoutConstraints() default @LayoutConstraints;
}
