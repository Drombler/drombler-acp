package org.drombler.acp.core.settings.spi;

import static org.drombler.acp.core.commons.util.BundleUtils.loadClass;
import org.drombler.acp.core.settings.jaxb.CustomSettingsCategoryType;
import static org.drombler.commons.client.util.ResourceBundleUtils.getClassResourceStringPrefixed;
import org.osgi.framework.Bundle;

public class CustomSettingsCategoryDescriptor<T> extends AbstractSettingsCategoryDescriptor {

    private final Class<T> contentPaneClass;

    public CustomSettingsCategoryDescriptor(String id, int position,  Class<T> contentPaneClass) {
        super(id, position);
        this.contentPaneClass = contentPaneClass;
    }

    /**
     * @return the contentPaneClass
     */
    public Class<T> getContentPaneClass() {
        return contentPaneClass;
    }

    public static CustomSettingsCategoryDescriptor<?> createCustomSettingsCategoryDescriptor(CustomSettingsCategoryType customSettingsCategory, Bundle bundle) throws ClassNotFoundException {
        Class<?> contentPaneClass = loadClass(bundle, customSettingsCategory.getContentPaneClass());
        return createCustomSettingsCategoryDescriptor(customSettingsCategory, contentPaneClass);
    }

    private static <T> CustomSettingsCategoryDescriptor<T> createCustomSettingsCategoryDescriptor(CustomSettingsCategoryType customSettingsCategory, Class<T> contentPaneClass) {
        CustomSettingsCategoryDescriptor<T> descriptor = new CustomSettingsCategoryDescriptor<>(customSettingsCategory.getId(), customSettingsCategory.getPosition(), contentPaneClass);
        descriptor.setDisplayName(getClassResourceStringPrefixed(contentPaneClass, customSettingsCategory.getDisplayName()));
        descriptor.setDisplayDescription(getClassResourceStringPrefixed(contentPaneClass, customSettingsCategory.getDisplayDescription()));
        AbstractSettingsCategoryDescriptor.configureSettingsCategoryDescriptor(descriptor, customSettingsCategory);
        return descriptor;
    }

}
