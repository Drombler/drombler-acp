package org.drombler.acp.core.data;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An abstract base class for directory-based {@link DataHandler}s.
 *
 * @author puce
 */
public abstract class AbstractDirectoryHandler extends AbstractPathHandler {
    /**
     * Creates a new instance of this class with no path set.
     */
    public AbstractDirectoryHandler() {
        this(null);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param directoryPath a directory path
     */
    public AbstractDirectoryHandler(Path directoryPath) {
        super(directoryPath);
        if (! Files.isDirectory(directoryPath)){
            throw new IllegalArgumentException("directoryPath must be a directory: " + directoryPath);
        }
    }

}
