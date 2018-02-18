package org.drombler.acp.core.settings.spi;

/**
 *
 * @author puce
 */
public interface SettingsCategoryContainer {

    void addCategory(ParentOnlySettingsCategoryDescriptor settingsCategoryDescriptor);

    void addCategory(CustomSettingsCategoryDescriptor<?> settingsCategoryDescriptor);
}
