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
public abstract class AbstractDocumentHandler extends AbstractDataHandler<Path> {

    public static final String PATH_PROPERTY_NAME = "path";

    private Path path;
    private final String defaultFileExtenion;
    private Path uniqueKey;

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
        setPath(path);
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
     * The path of the document or null if it does not exist yet.
     *
     * @return the path the path of the document or null if it does not exist yet
     */
    public Path getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    private void setPath(Path path) {
        if (this.path != null) {
            throw new IllegalStateException("The path must not change once set!");
        }
        this.path = path;
        setUniqueKey(path);
        getPropertyChangeSupport().firePropertyChange(PATH_PROPERTY_NAME, null, this.path);
        getPropertyChangeSupport().firePropertyChange(TITLE_PROPERTY_NAME, null, getTitle());
        getPropertyChangeSupport().firePropertyChange(TOOLTIP_TEXT_PROPERTY_NAME, null, getTooltipText());
    }

    @Override
    public String getTitle() {
        return getPath() != null ? getPath().getFileName().toString() : null;
    }

    @Override
    public String getTooltipText() {
        return getPath() != null ? getPath().toString() : null;
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

    @Override
    public Path getUniqueKey() {
        return uniqueKey;
    }

    private void setUniqueKey(Path path) {
        if (path != null && uniqueKey == null) {
            try {
                uniqueKey = path.toRealPath();
            } catch (IOException ex) {
                uniqueKey = path;
            }
            getPropertyChangeSupport().firePropertyChange(UNIQUE_KEY_PROPERTY_NAME, null, this.uniqueKey);
        }
    }

}
