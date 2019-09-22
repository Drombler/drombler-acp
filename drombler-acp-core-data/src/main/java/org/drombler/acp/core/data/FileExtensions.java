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
 * Copyright 2016 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.data;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The containing annotation type for the repeatable annotation {@link FileExtension}.<br>
 * <br>
 * Note: Since {@link FileExtension} is a {@link Repeatable} annotation, this containing annotation type is usually not needed to be declared explicitly.
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
public @interface FileExtensions {

    /**
     * An array of the repeatable annotation {@link FileExtension}.
     *
     * @return an array of the repeatable annotation {@link FileExtension}
     */
    FileExtension[] value();
}
