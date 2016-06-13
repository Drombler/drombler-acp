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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;
import org.drombler.commons.context.LocalContextProvider;

/**
 * Registers a document handler. A document handler usually knows how to read, save etc. a particular kind of document. A document handler is usally {@link Path} based.
 *
 * To fully integrate with the framework it should implement {@link LocalContextProvider}, observe registered {@link DataCapabilityProvider}s and add the found data capabilities to it's local context.
 *
 * The easiest way to implement a document handler is to extend AbstractDocumentHandler, but it's not required.
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface DocumentHandler {

    /**
     * The MIME type of the document.
     *
     * @return the MIME type of the document
     */
    String mimeType();

    /**
     * The icon name pattern to resolve the icons to be used for this file type handler.
     *
     * Note that this only specifies the name pattern. Drombler ACP looks for
     * <icon-base-name>16.<icon-extension> for data types (expected to be 16x16 pixels). So if icon is &quot;test.png&quot;, Drombler ACP would look for test16.png.
     *
     * @return the icon name pattern
     */
    String icon() default "";
}
