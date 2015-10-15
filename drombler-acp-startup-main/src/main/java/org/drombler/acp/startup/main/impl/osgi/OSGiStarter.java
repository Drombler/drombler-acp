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
import java.util.Map;
import java.util.ServiceLoader;
import org.drombler.acp.startup.main.ApplicationConfigProvider;
import org.drombler.acp.startup.main.ApplicationConfiguration;
import org.drombler.acp.startup.main.ServiceLoaderException;
import org.drombler.acp.startup.main.impl.ApplicationConfigProviderImpl;
import org.drombler.acp.startup.main.impl.PropertiesUtils;
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
        this.configMap = PropertiesUtils.toMap(configuration.getUserConfigProps());
        FrameworkFactory factory = getFrameworkFactory();
        this.framework = factory.newFramework(configMap);
    }

    private FrameworkFactory getFrameworkFactory() {
        for (FrameworkFactory frameworkFactory : FRAMEWORK_FACTORY_LOADER) {
            return frameworkFactory;
        }

        throw new ServiceLoaderException("Could not find framework factory.");
    }

    public void start() throws BundleException, InterruptedException {
        FrameworkEvent event;
        do {
            framework.start();
            event = framework.waitForStop(0);
        } while (event.getType() == FrameworkEvent.STOPPED_UPDATE);
    }

    public void init() throws BundleException, IOException {
        registerShutdownHook();
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

    private void registerShutdownHook() {
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
