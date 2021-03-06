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
import org.drombler.commons.docking.LayoutConstraintsDescriptor;

/**
 * The Layout constraints.
 *
 * @see LayoutConstraintsDescriptor
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({})
public @interface LayoutConstraints {

    /**
     * The preferred width of the Docking Area. Can be negative to indicate a flexible width.
     *
     * @return the preferred width of the Docking Area. Can be negative to indicate a flexible width.
     */
    double prefWidth() default -1;

    /**
     * The preferred height of the Docking Area. Can be negative to indicate a flexible height.
     *
     * @return the preferred height of the Docking Area. Can be negative to indicate a flexible height.
     */
    double prefHeight() default -1;
}
