package org.drombler.acp.core.data;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractDirectoryHandler extends AbstractPathHandler {

    public AbstractDirectoryHandler() {
        this(null);
    }
    
    public AbstractDirectoryHandler(Path directoryPath) {
        super(directoryPath);
        if (! Files.isDirectory(directoryPath)){
            throw new IllegalArgumentException("directoryPath must be a directory: " + directoryPath);
        }
    }

}
