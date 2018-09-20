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

import java.io.IOException;
import java.nio.file.Path;
import org.drombler.commons.data.DataCapabilityProvider;

/**
 * An abstract {@link Path}-based {@link DocumentHandler}. It observes registered {@link DataCapabilityProvider}s and adds the found data capabilities to it's local context.
 *
 * You can use this class as a base class for your own document handler implementations.
 *
 * @author puce
 */
public abstract class AbstractDocumentHandler extends AbstractPathHandler {

    private final String defaultFileExtenion;

    /**
     * Creates a new instance of this class with an unkown (null) document path.
     *
     * @param defaultFileExtenion the default file extension for the document type
     */
    public AbstractDocumentHandler(String defaultFileExtenion) {
        this(defaultFileExtenion, null);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param path the path to the document or null if unknown/ the document does not exist yet.
     * @param defaultFileExtenion the default file extension for the document type
     */
    public AbstractDocumentHandler(String defaultFileExtenion, Path path) {
        super(path);
        this.defaultFileExtenion = defaultFileExtenion;
//        SavableAs savableAs = new SavableAs() {
//            @Override
//            public void saveNew() {
//                AbstractDocumentHandler.this.saveNew("sfd");
//
//            }
//        };
    }

    /**
     * Gets the default file extension for the document type.
     *
     * @return the defaultFileExtenion the default file extension for the document type
     */
    public String getDefaultFileExtenion() {
        return defaultFileExtenion;
    }

    /**
     * Saves the content to the file. Only call this method if the path is known.
     *
     * @throws IOException
     * @see #saveNew(java.nio.file.Path)
     */
    public void save() throws IOException {
        writeContent();
    }

    /**
     * Saves the content to a file and updates the path property.
     *
     * The current path must be null!
     *
     * @param newPath the new document path
     * @throws IOException
     * @see #save()
     */
    // TODO: private
    public void saveNew(Path newPath) throws IOException {
        if (getPath() != null) {
            throw new IllegalStateException("The path must not change once set!");
        }
        setPath(newPath);
        save();
    }

    /**
     * Writes the content to the file.
     *
     * @throws IOException
     */
    protected abstract void writeContent() throws IOException;

}
