/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (GitHub user: puce77).
 * Copyright 2015 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.startup.main.impl.osgi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import org.drombler.acp.startup.main.ApplicationConfigProvider;
import org.drombler.acp.startup.main.ApplicationConfiguration;
import static org.drombler.acp.startup.main.Main.SHUTDOWN_HOOK_PROP;
import org.drombler.acp.startup.main.ServiceLoaderException;
import org.drombler.acp.startup.main.impl.ApplicationConfigProviderImpl;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

/**
 *
 * @author puce
 */
public class OSGiStarter {

    private static final ServiceLoader<FrameworkFactory> FRAMEWORK_FACTORY_LOADER = ServiceLoader.load(
            FrameworkFactory.class);
    private final ApplicationConfiguration configuration;
    private final Map<String, String> configMap;
    private final Framework framework;

    /**
     *
     * @param configuration
     */
    public OSGiStarter(ApplicationConfiguration configuration) {
        this.configuration = configuration;
        this.configMap = toMap(configuration.getUserConfigProps());
        FrameworkFactory factory = getFrameworkFactory();
        this.framework = factory.newFramework(configMap);
    }

    private FrameworkFactory getFrameworkFactory() {
        for (FrameworkFactory frameworkFactory : FRAMEWORK_FACTORY_LOADER) {
            return frameworkFactory;
        }

        throw new ServiceLoaderException("Could not find framework factory.");
    }

    public void start() {
        try {
            FrameworkEvent event;
            do {
                // Start the framework.
                framework.start();
                // Wait for framework to stop to exit the VM.
                event = framework.waitForStop(0);
            } // If the framework was updated, then restart it.
            while (event.getType() == FrameworkEvent.STOPPED_UPDATE);
            // Otherwise, exit.
            System.exit(0);
        } catch (Exception ex) {
            System.err.println("Could not create framework: " + ex);
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    public void init() throws BundleException, IOException {
        registerShutdownHook(configuration.getUserConfigProps());
        // Create an instance of the framework.
        // Initialize the framework, but don't start it yet.
        framework.init();
        initServices();
        // Use the system bundle context to process the auto-deploy
        // and auto-install/auto-start properties.
        AutoProcessor autoProcessor = new AutoProcessor();
        autoProcessor.process(framework, configMap, configuration.getInstallDirPath(),
                configuration.getUserDirPath());
    }

    private Map<String, String> toMap(Properties properties) {
        Map<String, String> map = new HashMap<>(properties.size());
        for (String propertyName : properties.stringPropertyNames()) {
            map.put(propertyName, properties.getProperty(propertyName));
        }
        return map;
    }

    private void registerShutdownHook(Properties userConfigProps) {
        // If enabled, register a shutdown hook to make sure the framework is
        // cleanly shutdown when the VM exits.
        String enableHook = userConfigProps.getProperty(SHUTDOWN_HOOK_PROP);
        if ((enableHook == null) || !enableHook.equalsIgnoreCase("false")) {
            Runtime.getRuntime().addShutdownHook(new Thread("Felix Shutdown Hook") {

                @Override
                public void run() {
                    try {
                        OSGiStarter.this.stop();
                    } catch (Exception ex) {
                        System.err.println("Error stopping framework: " + ex);
                    }
                }
            });
        }
    }

    private void initServices() {
        framework.getBundleContext().registerService(ApplicationConfigProvider.class,
                new ApplicationConfigProviderImpl(),
                null);
    }

    public void stop() throws BundleException, InterruptedException {
        if (framework != null) {
            framework.stop();
            framework.waitForStop(0);
        }
    }


}
