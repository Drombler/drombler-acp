package org.drombler.acp.core.settings.spi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.drombler.acp.core.settings.jaxb.AbstractSettingsCategoryType;

public abstract class AbstractSettingsCategoryDescriptor {

    private static final String PATH_DELIMITER = "/";

    private final String id;
    private final int position;

    private String displayName;
    private List<String> path = Collections.emptyList();
    private String displayDescription = "";

    public AbstractSettingsCategoryDescriptor(String id, int position) {
        this.id = id;
        this.position = position;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the path
     */
    public List<String> getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(List<String> path) {
        this.path = path;
    }

    /**
     * @return the displayDescription
     */
    public String getDisplayDescription() {
        return displayDescription;
    }

    /**
     * @param displayDescription the displayDescription to set
     */
    public void setDisplayDescription(String displayDescription) {
        this.displayDescription = displayDescription;
    }

    public static void configureSettingsCategoryDescriptor(AbstractSettingsCategoryDescriptor descriptor, AbstractSettingsCategoryType settingsCategory) {
        descriptor.setPath(splitPath(settingsCategory.getPath()));
    }

    private static List<String> splitPath(String path) {
        path = path.replaceFirst("^" + PATH_DELIMITER + "*", "");
        if (!path.equals("")) {
            return Arrays.asList(path.split("/"));
        } else {
            return Collections.emptyList();
        }
    }
}
