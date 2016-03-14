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

/**
 *
 * @author puce
 */
public @interface DocumentHandler {

    String mimeType();

    /**
     * The text to be displayed, e.g. as the text for filters in file dialogs. If the value starts with '%' the rest of
     * the value is interpreted as a property key and the value gets looked-up in the Bundle.properties file (or a
     * locale * specific derivation of this file), which has to be in the same package as the annotated data type.
     *
     * @return the text to be displayed for this action
     */
    String displayName();

    /**
     * The icon name pattern to resolve the icons to be used for this file type handler.
     *
     * Note that this only specifies the name pattern. Drombler ACP looks for
     * <icon-base-name>16.<icon-extension> for data types (expected to be 16x16 pixels). So if icon is
     * &quot;test.png&quot;, Drombler ACP would look for test16.png.
     *
     * @return the icon name pattern
     */
    String icon() default "";
}
