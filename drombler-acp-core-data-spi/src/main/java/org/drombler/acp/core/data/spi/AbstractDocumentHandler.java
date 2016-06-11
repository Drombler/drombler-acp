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
package org.drombler.acp.core.data.spi;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.osgi.util.tracker.ServiceTracker;

public abstract class AbstractDocumentHandler extends AbstractDataHandler {

    private final ServiceTracker<FileChooserProvider, FileChooserProvider> fileChooserProviderTracker;
    private Path path;
    private FileChooserProvider fileChooserProvider;

    public AbstractDocumentHandler() {
        this(null);
    }

    public AbstractDocumentHandler(Path path) {
        this.path = path;
        this.fileChooserProviderTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FileChooserProvider.class, this::setFileChooserProvider);

        this.fileChooserProviderTracker.open(true);
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

    public void save() throws IOException {
        writeContent();
    }

    public boolean saveAs(String initialFileName) throws IOException {
        Path selectedPath = getFileChooserProvider().showSaveAsDialog(initialFileName);
        if (selectedPath != null) {
            setPath(selectedPath);
            save();
            return true;
        } else {
            return false;
        }
    }

    protected abstract void writeContent() throws IOException;

    @Override
    public void close() {
        fileChooserProviderTracker.close();
        super.close();
    }

    /**
     * @return the fileChooserProvider
     */
    public FileChooserProvider getFileChooserProvider() {
        return fileChooserProvider;
    }

    /**
     * @param fileChooserProvider the fileChooserProvider to set
     */
    public void setFileChooserProvider(FileChooserProvider fileChooserProvider) {
        this.fileChooserProvider = fileChooserProvider;
    }

}
