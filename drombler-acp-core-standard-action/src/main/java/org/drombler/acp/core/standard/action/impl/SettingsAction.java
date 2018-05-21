package org.drombler.acp.core.standard.action.impl;

import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.settings.spi.SettingsCategoryContainer;
import org.drombler.commons.action.AbstractActionListener;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * @author puce
 */
@Action(id = "standard.settings", category = "core", displayName = "%settings.displayName", accelerator = "Shortcut+Alt+S")
@MenuEntry(path = "File", position = 5100)
public class SettingsAction extends AbstractActionListener<Object> implements AutoCloseable {

    private final ServiceTracker<SettingsCategoryContainer, SettingsCategoryContainer> settingsCategoryContainerServiceTracker;
    private SettingsCategoryContainer settingsCategoryContainer;

    public SettingsAction() {
        this.settingsCategoryContainerServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(SettingsCategoryContainer.class, this::setSettingsCategoryContainer);
        this.settingsCategoryContainerServiceTracker.open(true);
        setEnabled(isInitialized());
    }

    private boolean isInitialized() {
        return settingsCategoryContainer != null;
    }

    @Override
    public void onAction(Object event) {
        settingsCategoryContainer.openDialog();
    }

    private void setSettingsCategoryContainer(SettingsCategoryContainer settingsCategoryContainer) {
        this.settingsCategoryContainer = settingsCategoryContainer;
        setEnabled(isInitialized());
    }

    @Override
    public void close() {
        settingsCategoryContainerServiceTracker.close();
    }
}
