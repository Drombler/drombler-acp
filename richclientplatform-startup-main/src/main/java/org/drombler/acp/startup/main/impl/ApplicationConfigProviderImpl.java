/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.startup.main.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.drombler.acp.startup.main.ApplicationConfigProvider;

/**
 *
 * @author puce
 */
public class ApplicationConfigProviderImpl implements ApplicationConfigProvider {
    public static final String APPLICATION_PROPERTIES_FILE_PATH = "/applicationConfig.properties";
    private final Map<String, String> applicationConfig;

    public ApplicationConfigProviderImpl() {
        Properties configProperties = new Properties();

        try (InputStream is = ApplicationConfigProviderImpl.class.getResourceAsStream(APPLICATION_PROPERTIES_FILE_PATH)) {
            if (is != null) {
                configProperties.load(is);
            } 
        } catch (IOException ex) {
            System.err.println("ApplicationConfigProviderImpl: Error loading applicationConfig.properties!");
        }

        Map<String, String> config = new HashMap<>(configProperties.size());
        for (String propertyName : configProperties.stringPropertyNames()) {
            config.put(propertyName, configProperties.getProperty(propertyName));
        }
        applicationConfig = Collections.unmodifiableMap(config);
    }

    @Override
    public Map<String, String> getApplicationConfig() {
        return applicationConfig;
    }
}
