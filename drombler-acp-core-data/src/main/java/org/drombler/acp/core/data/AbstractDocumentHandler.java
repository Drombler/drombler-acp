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

import java.nio.file.Path;
import java.util.Locale;

public abstract class AbstractDocumentHandler extends AbstractDataHandler {

    private Path path;

    public AbstractDocumentHandler() {
        this(null);
    }

    public AbstractDocumentHandler(Path path) {
        this.path = path;

    }

    public String getDisplayString(Locale inLocale) {
        return getPath() != null ? getPath().getFileName().toString() : "???";
    }

    /**
     * @return the path
     */
    public Path getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(Path path) {
        this.path = path;
    }

}
