/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (GitHub user: puce77).
 * Copyright 2017 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.status;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.drombler.commons.client.geometry.HorizontalAlignment;

/**
 * This annotation registers a status bar element.
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface StatusBarElement {

    /**
     * The horizontal alignment.
     *
     * @return the horizontal alignment
     */
    HorizontalAlignment horizontalAlignment();

    /**
     * The position to order the menu items in a menu. It's a best practice to leave out some positions between entries
     * to allow other bundles to register entries between some existing ones.
     *
     * @return the position to order the menu items in a menu
     */
    int position();
}
