package org.drombler.acp.core.settings;

import org.drombler.commons.settings.Settings;
import org.drombler.commons.settings.SettingsLevel;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */


public interface SettingsProvider {
    Settings getSettings(SettingsLevel level, Bundle bundle);
}
