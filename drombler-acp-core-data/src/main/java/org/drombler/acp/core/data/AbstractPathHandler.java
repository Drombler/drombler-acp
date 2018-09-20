package org.drombler.acp.core.data;

import java.io.IOException;
import java.nio.file.Path;
import org.drombler.commons.data.DataHandler;

public abstract class AbstractPathHandler extends AbstractDataHandler<Path> {

    public static final String PATH_PROPERTY_NAME = "path";
    private Path path;
    private Path uniqueKey;


    public AbstractPathHandler() {
        this(null);
    }
    
    public AbstractPathHandler(Path path) {
        setPath(path);
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
    protected final void setPath(Path path) {
        if (this.path != null) {
            throw new IllegalStateException("The path must not change once set!");
        }
        this.path = path;
        setUniqueKey(path);
        getPropertyChangeSupport().firePropertyChange(PATH_PROPERTY_NAME, null, this.path);
        getPropertyChangeSupport().firePropertyChange(DataHandler.TITLE_PROPERTY_NAME, null, getTitle());
        getPropertyChangeSupport().firePropertyChange(DataHandler.TOOLTIP_TEXT_PROPERTY_NAME, null, getTooltipText());
    }

    @Override
    public String getTitle() {
        return getPath() != null ? getPath().getFileName().toString() : null;
    }

    @Override
    public String getTooltipText() {
        return getPath() != null ? getPath().toString() : null;
    }

    @Override
    public Path getUniqueKey() {
        return uniqueKey;
    }

    protected void setUniqueKey(Path path) {
        if (path != null && uniqueKey == null) {
            try {
                uniqueKey = path.toRealPath();
            } catch (IOException ex) {
                uniqueKey = path;
            }
            getPropertyChangeSupport().firePropertyChange(DataHandler.UNIQUE_KEY_PROPERTY_NAME, null, this.uniqueKey);
        }
    }
}
