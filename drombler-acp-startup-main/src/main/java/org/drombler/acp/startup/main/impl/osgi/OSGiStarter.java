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
import java.text.MessageFormat;
import java.util.Map;
import java.util.ServiceLoader;
import org.drombler.acp.startup.main.DromblerACPConfiguration;
import org.drombler.acp.startup.main.ServiceLoaderException;
import org.drombler.acp.startup.main.impl.PropertiesUtils;
import org.drombler.commons.client.startup.main.BootServiceStarter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

/**
 *
 * @author puce
 */
public class OSGiStarter implements BootServiceStarter {

    private static final ServiceLoader<FrameworkFactory> FRAMEWORK_FACTORY_LOADER = ServiceLoader.load(
            FrameworkFactory.class);
    private final DromblerACPConfiguration configuration;
    private final Map<String, String> configMap;
    private final Framework framework;
    private final boolean active;

    /**
     *
     * @param configuration
     * @param active
     */
    public OSGiStarter(DromblerACPConfiguration configuration, boolean active) {
        this.configuration = configuration;
        this.configMap = PropertiesUtils.toMap(configuration.getUserConfigProps());
        FrameworkFactory factory = getFrameworkFactory();
        this.framework = factory.newFramework(configMap);
        this.active = active;
    }

    private FrameworkFactory getFrameworkFactory() {
        for (FrameworkFactory frameworkFactory : FRAMEWORK_FACTORY_LOADER) {
            return frameworkFactory;
        }

        throw new ServiceLoaderException("Could not find framework factory.");
    }

    @Override
    public String getName() {
        return "Felix Starter";
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean init() throws BundleException, IOException {
        // Create an instance of the framework.
        // Initialize the framework, but don't start it yet.
        getFramework().init();
        // Use the system bundle context to process the auto-deploy
        // and auto-install/auto-start properties.
        AutoProcessor autoProcessor = new AutoProcessor();
        autoProcessor.process(getFramework(), configMap, configuration.getInstallDirPath(),
                configuration.getUserDirPath());
        return true;
    }

    @Override
    public void startAndWait() throws BundleException, InterruptedException {
        FrameworkEvent event;
        do {
            logInfo("Starting OSGi Framework...");
            getFramework().start();
            event = getFramework().waitForStop(0);
        } while (event.getType() == FrameworkEvent.STOPPED_UPDATE);
    }

    @Override
    public void stop() throws BundleException, InterruptedException {
        if (isRunning()) {
            getFramework().stop();
            FrameworkEvent stopEvent = getFramework().waitForStop(0);
            System.out.println("OSGi Framework stopped: " + stopEvent.getType());
        }
    }

    /**
     * @return the framework
     */
    public Framework getFramework() {
        return framework;
    }

    private void logInfo(String messageFormat, Object... arguments) {
        // TODO: replace with SLF4J Logger once available on classpath
        // Note: the message format is different!
        System.out.println(MessageFormat.format(messageFormat, arguments));
    }

    @Override
    public boolean isRunning() {
        return getFramework().getState() == Bundle.ACTIVE;
    }

    /**
     * This starter is required for OSGi applications.
     * @return true
     */
    @Override
    public boolean isRequired() {
        return true;
    }

}
