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
package org.drombler.acp.startup.main;

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
import java.util.Properties;
import org.apache.felix.framework.util.Util;

/**
 *
 * @author puce
 */
public class DromblerACPConfiguration {

    /**
     * The jar URI prefix "jar:"
     */
    private static final String FULL_JAR_URI_PREFIX = "jar:";
    /**
     * Length of the jar URI prefix "jar:"
     */
    private static final int FULL_JAR_URI_PREFIX_LENGTH = 4;

    /**
     * The property name used to specify an URL to the system property file.
     *
     */
    public static final String SYSTEM_PROPERTIES_PROP = "system.properties.file";

    /**
     * The default name used for the system properties file.
     *
     */
    public static final String SYSTEM_PROPERTIES_FILE_NAME = "system.properties";

    /**
     * Name of the configuration directory.
     */
    public static final String CONFIG_DIRECTORY_NAME = "conf";

    /**
     * The property name used to specify an URL to the configuration property file to be used for the created the framework instance.
     *
     */
    public static final String CONFIG_PROPERTIES_PROP = "config.properties.file";
    /**
     * The default name used for the configuration properties file.
     *
     */
    public static final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";

    public static final String USER_DIR_PROPERTY = "drombler.application.userdir";

    private final Path installDirPath;
    private final Path installConfigDirPath;
    private final Path userDirPath;
    private final Path userConfigDirPath;

    private final Properties userConfigProps;
    private final ApplicationConfiguration applicationConfig;
    private final CommandLineArgs commandLineArgs;

    /**
     *
     * @param commandLineArgs
     * @throws URISyntaxException
     * @throws IOException
     * @throws MissingPropertyException
     */
    public DromblerACPConfiguration(CommandLineArgs commandLineArgs) throws URISyntaxException, IOException,
            MissingPropertyException {
        this.commandLineArgs = commandLineArgs;
        this.installDirPath = determineInstallDirPath();
        this.installConfigDirPath = installDirPath.resolve(CONFIG_DIRECTORY_NAME);

        loadSystemProperties(getInstallDirPath());

        Properties defaultConfigProps = loadDefaultConfigProps();
        Properties installConfigProps = createInstallConfigProps(defaultConfigProps, commandLineArgs);
        resolveProperties(installConfigProps);

        this.userDirPath = determineUserDirPath(installConfigProps);
        if (!Files.exists(userDirPath)) {
            Files.createDirectories(userDirPath);
        }
        System.out.println("User dir: " + userDirPath);
        this.userConfigProps = new Properties(installConfigProps);
        loadConfigProperties(userConfigProps, userDirPath);

        resolveProperties(userConfigProps);
        copySystemProperties(userConfigProps);

        this.userConfigDirPath = userDirPath.resolve(CONFIG_DIRECTORY_NAME);
        if (!Files.exists(userConfigDirPath)) {
            Files.createDirectories(userConfigDirPath);
        }
        this.applicationConfig = new ApplicationConfiguration();
    }

    private Properties createInstallConfigProps(Properties defaultConfigProps, CommandLineArgs commandLineArgs) throws
            IOException {
        Properties installConfigProps = new Properties(defaultConfigProps);
        loadConfigProperties(installConfigProps, getInstallDirPath());
        overrideInstallConfigProps(installConfigProps, commandLineArgs);
        return installConfigProps;
    }

    private Path determineInstallDirPath() throws URISyntaxException {
        Class<DromblerACPStarter> type = DromblerACPStarter.class;
        String jarResourceURIString = type.getResource("/" + type.getName().replace(".", "/") + ".class").toURI().
                toString();
        int endOfJarPathIndex = jarResourceURIString.indexOf("!/");
        String mainJarURIString = endOfJarPathIndex >= 0 ? jarResourceURIString.substring(0, endOfJarPathIndex)
                : jarResourceURIString;
        if (mainJarURIString.startsWith(FULL_JAR_URI_PREFIX)) {
            mainJarURIString = mainJarURIString.substring(FULL_JAR_URI_PREFIX_LENGTH);
        }
        Path mainJarPath = Paths.get(URI.create(mainJarURIString));
        // <install-dir>/bin/lib/<jar>
        return mainJarPath.getParent().getParent().getParent();
    }

    protected void loadSystemProperties(Path rootDirPath) throws MalformedURLException, IOException {
        Properties props = new Properties();
        loadProperties(props, SYSTEM_PROPERTIES_PROP, rootDirPath, SYSTEM_PROPERTIES_FILE_NAME);

        for (String propertyName : props.stringPropertyNames()) {
            System.setProperty(propertyName, Util.substVars(props.getProperty(propertyName), propertyName, null, null));
        }

    }

    private void loadConfigProperties(Properties configProps, Path rootDirPath) throws MalformedURLException,
            IOException {
        loadProperties(configProps, CONFIG_PROPERTIES_PROP, rootDirPath, CONFIG_PROPERTIES_FILE_NAME);
    }

    private void loadProperties(Properties props, String systemPropertyName, Path rootDirPath, String propertiesFileName)
            throws IOException, MalformedURLException {
        String custom = System.getProperty(systemPropertyName);
        URL propURL = custom != null ? new URL(custom) : rootDirPath.resolve(CONFIG_DIRECTORY_NAME).resolve(
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

    protected Properties loadDefaultConfigProps() throws IOException {
        Properties props = new Properties();
        try (InputStream is = DromblerACPConfiguration.class.getResourceAsStream("config.properties")) {
            props.load(is);
        }
        return props;
    }

    private void overrideInstallConfigProps(Properties installConfigProps, CommandLineArgs commandLineArgs) {
        if (commandLineArgs.getUserDir() != null) {
            installConfigProps.setProperty(USER_DIR_PROPERTY, commandLineArgs.getUserDir());
        }
    }

    private Path determineUserDirPath(Properties installConfigProps) throws MissingPropertyException {
        String userDirName = installConfigProps.getProperty(USER_DIR_PROPERTY);
        if (userDirName == null) {
            throw new MissingPropertyException("Undefined property: " + USER_DIR_PROPERTY);
        }

        return Paths.get(userDirName);
    }

    private void resolveProperties(Properties configProps) throws IllegalArgumentException {
        for (String propertyName : configProps.stringPropertyNames()) {
            configProps.setProperty(propertyName,
                    Util.substVars(configProps.getProperty(propertyName), propertyName, null, configProps));
        }
    }

    private void copySystemProperties(Properties configProps) {
        System.getProperties().stringPropertyNames().stream().
                filter((propertyName)
                        -> (propertyName.startsWith("felix.") || propertyName.startsWith("org.osgi.framework."))).
                forEach((propertyName) -> configProps.setProperty(propertyName, System.getProperty(propertyName)));
    }

    /**
     * @return the installDirPath
     */
    public final Path getInstallDirPath() {
        return installDirPath;
    }

    public final Path getInstallConfigDirPath() {
        return installConfigDirPath;
    }

    /**
     * @return the userDirPath
     */
    public final Path getUserDirPath() {
        return userDirPath;
    }

    public final Path getUserConfigDirPath() {
        return userConfigDirPath;
    }

    /**
     * @return the userConfigProps
     */
    public Properties getUserConfigProps() {
        return userConfigProps;
    }

    /**
     * @return the applicationConfig
     */
    public ApplicationConfiguration getApplicationConfig() {
        return applicationConfig;
    }

    /**
     * @return the commandLineArgs
     */
    public CommandLineArgs getCommandLineArgs() {
        return commandLineArgs;
    }

}
