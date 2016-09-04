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

import org.drombler.commons.client.dialog.FileChooserProvider;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.osgi.util.tracker.ServiceTracker;

/**
 * An abstract {@link Path}-based {@link DocumentHandler}. It observes registered {@link DataCapabilityProvider}s and adds the found data capabilities to it's local context.
 *
 * You can use this class as a base class for your own document handler implementations.
 *
 * @author puce
 */
public abstract class AbstractDocumentHandler extends AbstractDataHandler<Path> {

    private final ServiceTracker<FileChooserProvider, FileChooserProvider> fileChooserProviderTracker;
    private Path path;
    private FileChooserProvider fileChooserProvider;
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
        this.fileChooserProviderTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FileChooserProvider.class, this::setFileChooserProvider);

        this.fileChooserProviderTracker.open(true);
//        SavableAs savableAs = new SavableAs() {
//            @Override
//            public void saveAs() {
//                AbstractDocumentHandler.this.saveAs("sfd");
//
//            }
//        };
    }

    private String getDisplayString(Locale inLocale) {
        return getPath() != null ? getPath().getFileName().toString() : "???";
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
     * @see #saveAs(java.lang.String)
     */
    public void save() throws IOException {
        writeContent();
    }

    /**
     * Saves the content to a file selected from the saveAs dialog. If a file has been selected, the path is set to the selected file.
     *
     * @param initialFileName the initial file name
     * @return true, if a file has been selected and saved, else false
     * @throws IOException
     * @see #save()
     */
    // TODO: private
    public boolean saveAs(String initialFileName) throws IOException {
        Path selectedPath = getFileChooserProvider().showSaveAsDialog(initialFileName);
        if (selectedPath != null) {
            setPath(selectedPath); // attention IllegalState!!
            save();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Writes the content to the file.
     *
     * @throws IOException
     */
    protected abstract void writeContent() throws IOException;

    /**
     * {@inheritDoc }
     */
    @Override
    public void close() {
        fileChooserProviderTracker.close();
        super.close();
    }

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
        }
    }

    /**
     * The system wide FileChooserProvider used for the saveAs dialog.
     *
     * @return the fileChooserProvider the system wide FileChooserProvider
     */
    protected FileChooserProvider getFileChooserProvider() {
        return fileChooserProvider;
    }

    private void setFileChooserProvider(FileChooserProvider fileChooserProvider) {
        this.fileChooserProvider = fileChooserProvider;
    }

}
