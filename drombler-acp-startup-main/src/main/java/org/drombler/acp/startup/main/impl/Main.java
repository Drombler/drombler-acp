/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2012 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.startup.main.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import org.apache.felix.framework.util.Util;
import org.drombler.acp.startup.main.ApplicationConfigProvider;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class Main {

//    private static final Logger LOG = LoggerFactory.getLogger(Main.class); // TODO: outside OSGi Framework...?
    public static final String USER_DIR_PROPERTY = "platform.userdir";

    /**
     * The property name used to specify whether the launcher should install a shutdown hook.
     *
     */
    public static final String SHUTDOWN_HOOK_PROP = "felix.shutdown.hook";
    /**
     * The property name used to specify an URL to the system property file.
     *
     */
    public static final String SYSTEM_PROPERTIES_PROP = "felix.system.properties";
    /**
     * The default name used for the system properties file.
     *
     */
    public static final String SYSTEM_PROPERTIES_FILE_NAME = "system.properties";
    /**
     * The property name used to specify an URL to the configuration property file to be used for the created the
     * framework instance.
     *
     */
    public static final String CONFIG_PROPERTIES_PROP = "felix.config.properties";
    /**
     * The default name used for the configuration properties file.
     *
     */
    public static final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";
    /**
     * Name of the configuration directory.
     */
    public static final String CONFIG_DIRECTORY = "conf";
    /**
     * The jar URI prefix "jar:"
     */
    private static final String FULL_JAR_URI_PREFIX = "jar:";
    /**
     * Length of the jar URI prefix "jar:"
     */
    private static final int FULL_JAR_URI_PREFIX_LENGTH = 4;
    private static final ServiceLoader<FrameworkFactory> frameworkFactoryLoader = ServiceLoader.load(
            FrameworkFactory.class);
    private static Framework m_fwk = null;

    public static void main(String[] args) throws Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parseCommandLineArgs(args);
        Main main = new Main();
        main.start(commandLineArgs);
    }

    public void start(CommandLineArgs commandLineArgs) throws URISyntaxException, MalformedURLException, IOException,
            MissingPropertyException {
        Path installDirPath = getInstallDirPath();

        loadSystemProperties(installDirPath);

        Properties defaultConfigProps = getDefaultConfigProps();
        Properties installConfigProps = new Properties(defaultConfigProps);
        loadConfigProperties(installConfigProps, installDirPath);
        overrideInstallConfigProps(installConfigProps, commandLineArgs);

        Path userDirPath = getUserDirPath(installConfigProps);
        if (!Files.exists(userDirPath)) {
            Files.createDirectories(userDirPath);
        }
        System.out.println("User dir: " + userDirPath);
        Properties userConfigProps = new Properties(installConfigProps);
        loadConfigProperties(userConfigProps, userDirPath);

        resolveProperties(userConfigProps);
        copySystemProperties(userConfigProps);

        registerShutdownHook(userConfigProps);

        try {
            // Create an instance of the framework.
            FrameworkFactory factory = getFrameworkFactory();
            Map<String, String> configMap = new HashMap<>(userConfigProps.size());
            for (String propertyName : userConfigProps.stringPropertyNames()) {
                configMap.put(propertyName, userConfigProps.getProperty(propertyName));
            }
            m_fwk = factory.newFramework(configMap);
            // Initialize the framework, but don't start it yet.
            m_fwk.init();
            initServices();
            // Use the system bundle context to process the auto-deploy
            // and auto-install/auto-start properties.
            AutoProcessor autoProcessor = new AutoProcessor();
            autoProcessor.process(m_fwk, configMap, installDirPath, userDirPath);
            FrameworkEvent event;
            do {
                // Start the framework.
                m_fwk.start();
                // Wait for framework to stop to exit the VM.
                event = m_fwk.waitForStop(0);
            } // If the framework was updated, then restart it.
            while (event.getType() == FrameworkEvent.STOPPED_UPDATE);
            // Otherwise, exit.
            System.exit(0);
        } catch (Exception ex) {
            System.err.println("Could not create framework: " + ex);
            ex.printStackTrace();
            System.exit(0);
        }
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
                        if (m_fwk != null) {
                            m_fwk.stop();
                            m_fwk.waitForStop(0);
                        }
                    } catch (Exception ex) {
                        System.err.println("Error stopping framework: " + ex);
                    }
                }
            });
        }
    }

    private void resolveProperties(Properties configProps) throws IllegalArgumentException {
        for (String propertyName : configProps.stringPropertyNames()) {
            configProps.setProperty(propertyName,
                    Util.substVars(configProps.getProperty(propertyName), propertyName, null, configProps));
        }
    }

    private void overrideInstallConfigProps(Properties installConfigProps, CommandLineArgs commandLineArgs) {
        if (commandLineArgs.getUserDir() != null) {
            installConfigProps.setProperty(USER_DIR_PROPERTY, commandLineArgs.getUserDir());
        }
    }

    private Path getUserDirPath(Properties installConfigProps) throws MissingPropertyException {
        String userDirName = installConfigProps.getProperty(USER_DIR_PROPERTY);
        if (userDirName == null) {
            throw new MissingPropertyException("Undefined property: " + USER_DIR_PROPERTY);
        }
        return Paths.get(userDirName);
    }

    private FrameworkFactory getFrameworkFactory() throws Exception {
        for (FrameworkFactory frameworkFactory : frameworkFactoryLoader) {
            return frameworkFactory;
        }

        throw new Exception("Could not find framework factory.");
    }

    protected void loadSystemProperties(Path rootDirPath) throws MalformedURLException, IOException {
        Properties props = new Properties();
        loadProperties(props, SYSTEM_PROPERTIES_PROP, rootDirPath, SYSTEM_PROPERTIES_FILE_NAME);

        for (String propertyName : props.stringPropertyNames()) {
            System.setProperty(propertyName, Util.substVars(props.getProperty(propertyName), propertyName, null, null));
        }

    }

    private void loadProperties(Properties props, String systemPropertyName, Path rootDirPath, String propertiesFileName)
            throws IOException, MalformedURLException {
        String custom = System.getProperty(systemPropertyName);
        URL propURL = custom != null ? new URL(custom) : rootDirPath.resolve(CONFIG_DIRECTORY).resolve(
                propertiesFileName).toUri().toURL();

        try (InputStream is = propURL.openConnection().getInputStream()) {
            props.load(is);
        } catch (FileNotFoundException ex) {
            // Ignore file not found.
        } catch (IOException ex) {
            System.err.println(
                    "Main: Error loading system properties from " + propURL);
            throw ex;
        }
    }

    private void loadConfigProperties(Properties configProps, Path rootDirPath) throws MalformedURLException,
            IOException {
        loadProperties(configProps, CONFIG_PROPERTIES_PROP, rootDirPath, CONFIG_PROPERTIES_FILE_NAME);
    }

    private void copySystemProperties(Properties configProps) {
        System.getProperties().stringPropertyNames().stream().
                filter((propertyName)
                        -> (propertyName.startsWith("felix.") || propertyName.startsWith("org.osgi.framework."))).
                forEach((propertyName) -> configProps.setProperty(propertyName, System.getProperty(propertyName)));
    }

    private Path getInstallDirPath() throws URISyntaxException {
        Class<Main> type = Main.class;
        String jarResourceURIString = type.getResource("/" + type.getName().replace(".", "/") + ".class").toURI().
                toString();
        int endOfJarPathIndex = jarResourceURIString.indexOf("!/");
        String mainJarURIString = endOfJarPathIndex >= 0 ? jarResourceURIString.substring(0, endOfJarPathIndex)
                : jarResourceURIString;
        if (mainJarURIString.startsWith(FULL_JAR_URI_PREFIX)) {
            mainJarURIString = mainJarURIString.substring(FULL_JAR_URI_PREFIX_LENGTH);
        }
        Path mainJarPath = Paths.get(URI.create(mainJarURIString));
        // <install-dir>/bin/<main-jar>.jar
        return mainJarPath.getParent().getParent();
    }

    protected Properties getDefaultConfigProps() throws IOException {
        Properties props = new Properties();
        try (InputStream is = Main.class.getResourceAsStream("config.properties")) {
            props.load(is);
        }
        return props;
    }

    private void initServices() throws IOException {
        m_fwk.getBundleContext().registerService(ApplicationConfigProvider.class, new ApplicationConfigProviderImpl(),
                null);
    }
}
