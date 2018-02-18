package org.drombler.acp.core.settings.spi;

/**
 *
 * @author puce
 */
public interface SettingsCategoryContainer<T> {

    void addCategory(ParentOnlySettingsCategoryDescriptor settingsCategoryDescriptor);

    void addCategory(CustomSettingsCategoryDescriptor<? extends T> settingsCategoryDescriptor);
}
