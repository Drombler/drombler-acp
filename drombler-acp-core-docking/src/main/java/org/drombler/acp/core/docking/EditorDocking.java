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
import org.softsmithy.lib.util.UniqueKeyProvider;

/**
 * Registers an editor dockable.<br>
 * <br>
 * The annotated editor requires a constructor with a single parameter of type {@link #contentType() }. The contentType is usually a data handler.<br>
 * <br>
 * A new editor dockable will be created for each provided content (see the Docking SPI package for helper classes).
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface EditorDocking {

    /**
     * The content type of this editor. This is usually a data handler.
     *
     * @return the content type of this editor
     */
    Class<? extends UniqueKeyProvider<?>> contentType();

}
