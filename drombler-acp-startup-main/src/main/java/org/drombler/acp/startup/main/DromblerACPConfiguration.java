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

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Properties;
import org.apache.felix.framework.util.Util;
import org.drombler.commons.client.startup.main.DromblerClientConfiguration;
import org.drombler.commons.client.startup.main.MissingPropertyException;
import org.drombler.commons.client.startup.main.cli.CommandLineArgs;

/**
 * The Drombler ACP configuration. Subclasses may add additional properties or change the configuration such as the application layout.
 */
public class DromblerACPConfiguration extends DromblerClientConfiguration {

    /**
     * Creates a new instance of this class.
     *
     * @param commandLineArgs the command line args
     * @throws URISyntaxException
     * @throws IOException
     * @throws MissingPropertyException
     */
    public DromblerACPConfiguration(CommandLineArgs commandLineArgs) throws URISyntaxException, IOException,
            MissingPropertyException {
        super(commandLineArgs);
    }

    /**
     * Determines the installation directory path of this application. Drombler ACP expects the following application installation layout: {@code <install-dir>/bin/lib/<jar>}
     *
     * @param mainJarPath the path to the JAR file containing the main starter
     * @return the installation directory path of this application
     */
    @Override
    protected Path determineInstallDirPath(Path mainJarPath) {
        // <install-dir>/bin/lib/<jar>
        return mainJarPath.getParent().getParent().getParent();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected Properties loadDefaultConfigProps() throws IOException {
        Properties properties = new Properties(super.loadDefaultConfigProps());
        try (InputStream is = DromblerACPConfiguration.class.getResourceAsStream("config.properties")) {
            properties.load(is);
        }
        return properties;
    }

    /**
     * Resolves properties with variables.
     *
     * @param configProps the properties to resolve
     */
    @Override
    protected void resolveProperties(Properties configProps) throws IllegalArgumentException {
        super.resolveProperties(configProps);
        for (String propertyName : configProps.stringPropertyNames()) {
            configProps.setProperty(propertyName,
                    Util.substVars(configProps.getProperty(propertyName), propertyName, null, configProps));
        }
    }

    /**
     * Copies and sets some additional system properties to the provided config properties.
       *
     * @param configProps the source properties to copy to
     */
    @Override
    protected void copySystemProperties(Properties configProps) {
        super.copySystemProperties(configProps);

        System.getProperties().stringPropertyNames().stream().
                filter((propertyName)
                        -> (propertyName.startsWith("felix.") || propertyName.startsWith("org.osgi.framework."))).
                forEach((propertyName) -> configProps.setProperty(propertyName, System.getProperty(propertyName)));
    }

}
