package org.drombler.acp.core.data;

import java.io.IOException;
import java.nio.file.Path;
import org.drombler.commons.data.DataHandler;

/**
 * An abstract base class for {@link Path}-based {@link DataHandler}s.
 *
 * @author puce
 */
public abstract class AbstractPathHandler extends AbstractDataHandler<Path> {

    /**
     * The name of the 'uniqueKey' property.
     *
     * @see #getUniqueKey()
     */
    public static final String PATH_PROPERTY_NAME = "path";
    private Path path;
    private Path uniqueKey;

    /**
     * Creates a new instance of this class with no path set.
     */
    public AbstractPathHandler() {
        this(null);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param path the path this data handler should handle
     */
    public AbstractPathHandler(Path path) {
        setPath(path);
    }

    /**
     * Gets the path this data handler should handle or null if it does not exist yet.<br>
     * <br>
     * This is a bound property.
     *
     * @return the path the path this data handler should handle or null if it does not exist yet
     */
    public Path getPath() {
        return path;
    }

    /**
     * Sets the path this data handler should handle.<br>
     * <br>
     * This metheod must only called once with a non-null value.<br>
     * <br>
     * This is a bound property.
     *
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

    /**
     * Gets the file name of the path as title.<br>
     * <br>
     * This is a bound property.
     *
     * @return the file name of the path as title
     * @see #TITLE_PROPERTY_NAME
     *
     */
    @Override
    public String getTitle() {
        return getPath() != null ? getPath().getFileName().toString() : null;
    }

    /**
     * Gets the path as tooltip text.<br>
     * <br>
     * This is a bound property.
     *
     * @return the path as tooltip text
     * @see #TOOLTIP_TEXT_PROPERTY_NAME
     *
     */
    @Override
    public String getTooltipText() {
        return getPath() != null ? getPath().toString() : null;
    }

    /**
     * Gets the (real) path as unique key. Once a non-null unique key is returned, the key must not change!<br>
     * <br>
     * This is a bound property.
     *
     * @return the (real) path as unique key
     * @see #UNIQUE_KEY_PROPERTY_NAME
     * @see Path#toRealPath(java.nio.file.LinkOption...)
     *
     */
    @Override
    public Path getUniqueKey() {
        return uniqueKey;
    }

    /**
     * Sets the (real) path as unique key.<br>
     * <br>
     * This metheod must only called once with a non-null value.<br>
     * <br>
     * This is a bound property.
     *
     * @param path the path to use as unique key
     * @see #UNIQUE_KEY_PROPERTY_NAME
     * @see Path#toRealPath(java.nio.file.LinkOption...)
     *
     */
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
