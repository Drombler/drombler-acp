package org.drombler.acp.core.settings.spi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.prefs.Preferences;
import javax.xml.bind.JAXBException;
import org.drombler.acp.startup.main.DromblerACPConfiguration;
import org.drombler.commons.settings.xml.XmlPreferences;
import org.drombler.commons.settings.xml.XmlSettingsStorage;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 *
 * @author puce
 */
public class Settings {

    public static Preferences getPreferencesForBundle(Class<?> typeInBundle) throws JAXBException, IOException {
        DromblerACPConfiguration configuration;
        Bundle bundle = FrameworkUtil.getBundle(typeInBundle);
        
        System.out.println("DataFile base dir (bundle): " + bundle.getDataFile(""));
        System.out.println("DataFile base dir (bundleContext): " + bundle.getBundleContext().getDataFile(""));
        if (bundle != null) {
            String symbolicName = bundle.getSymbolicName();
            Path bundleSettingsFilePath = configuration.getUserDirPath().resolve("settings").resolve(symbolicName + "-settings.xml");
            XmlSettingsStorage settingsStorage = new XmlSettingsStorage(bundleSettingsFilePath);
            if (Files.exists(bundleSettingsFilePath)) {
                settingsStorage.load();
            }
            XmlPreferences xmlPreferences = new XmlPreferences(settingsStorage, null, symbolicName);
            String packagePath = typeInBundle.getPackage().getName().replace('.', '/');
            return xmlPreferences.node(packagePath);
        }
    }
}
}
