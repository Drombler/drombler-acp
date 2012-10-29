/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.drombler.acp.startup.main.ApplicationConfigProvider;

/**
 * <p> This class is the default way to instantiate and execute the framework. It is not intended to be the only way to
 * instantiate and execute the framework; rather, it is one example of how to do so. When embedding the framework in a
 * host application, this class can serve as a simple guide of how to do so. It may even be worthwhile to reuse some of
 * its property handling capabilities. </p>
 *
 */
public class Main {

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
    public static final String SYSTEM_PROPERTIES_FILE_VALUE = "system.properties";
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
    public static final String CONFIG_PROPERTIES_FILE_VALUE = "config.properties";
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

    /**
     * <p>
     * This method performs the main task of constructing an framework instance
     * and starting its execution. The following functions are performed
     * when invoked:
     * </p>
     * <ol>
     * <li><i><b>Examine and verify command-line arguments.</b></i> The launcher
     * accepts a "<tt>-b</tt>" command line switch to set the bundle auto-deploy
     * directory and a single argument to set the bundle cache directory.
     * </li>
     * <li><i><b>Read the system properties file.</b></i> This is a file
     * containing properties to be pushed into <tt>System.setProperty()</tt>
     * before starting the framework. This mechanism is mainly shorthand
     * for people starting the framework from the command line to avoid having
     * to specify a bunch of <tt>-D</tt> system property definitions.
     * The only properties defined in this file that will impact the framework's
     * behavior are the those concerning setting HTTP proxies, such as
     * <tt>http.proxyHost</tt>, <tt>http.proxyPort</tt>, and
     * <tt>http.proxyAuth</tt>. Generally speaking, the framework does
     * not use system properties at all.
     * </li>
     * <li><i><b>Read the framework's configuration property file.</b></i> This is
     * a file containing properties used to configure the framework
     * instance and to pass configuration information into
     * bundles installed into the framework instance. The configuration
     * property file is called <tt>config.properties</tt> by default
     * and is located in the <tt>conf/</tt> directory of the Felix
     * installation directory, which is the parent directory of the
     * directory containing the <tt>felix.jar</tt> file. It is possible
     * to use a different location for the property file by specifying
     * the desired URL using the <tt>felix.config.properties</tt>
     * system property; this should be set using the <tt>-D</tt> syntax
     * when executing the JVM. If the <tt>config.properties</tt> file
     * cannot be found, then default values are used for all configuration
     * properties. Refer to the
     * <a href="Felix.html#Felix(java.util.Map)"><tt>Felix</tt></a>
     * constructor documentation for more information on framework
     * configuration properties.
     * </li>
     * <li><i><b>Copy configuration properties specified as system properties
     * into the set of configuration properties.</b></i> Even though the
     * Felix framework does not consult system properties for configuration
     * information, sometimes it is convenient to specify them on the command
     * line when launching Felix. To make this possible, the Felix launcher
     * copies any configuration properties specified as system properties
     * into the set of configuration properties passed into Felix.
     * </li>
     * <li><i><b>Add shutdown hook.</b></i> To make sure the framework shutdowns
     * cleanly, the launcher installs a shutdown hook; this can be disabled
     * with the <tt>felix.shutdown.hook</tt> configuration property.
     * </li>
     * <li><i><b>Create and initialize a framework instance.</b></i> The OSGi standard
     * <tt>FrameworkFactory</tt> is retrieved from <tt>META-INF/services</tt>
     * and used to create a framework instance with the configuration properties.
     * </li>
     * <li><i><b>Auto-deploy bundles.</b></i> All bundles in the auto-deploy
     * directory are deployed into the framework instance.
     * </li>
     * <li><i><b>Start the framework.</b></i> The framework is started and
     * the launcher thread waits for the framework to shutdown.
     * </li>
     * </ol>
     * <p>
     * It should be noted that simply starting an instance of the framework is not
     * enough to create an interactive session with it. It is necessary to install
     * and start bundles that provide a some means to interact with the framework;
     * this is generally done by bundles in the auto-deploy directory or specifying
     * an "auto-start" property in the configuration property file. If no bundles
     * providing a means to interact with the framework are installed or if the
     * configuration property file cannot be found, the framework will appear to
     * be hung or deadlocked. This is not the case, it is executing correctly,
     * there is just no way to interact with it.
     * </p>
     * <p>
     * The launcher provides two ways to deploy bundles into a framework at
     * startup, which have associated configuration properties:
     * </p>
     * <ul>
     * <li>Bundle auto-deploy - Automatically deploys all bundles from a
     * specified directory, controlled by the following configuration
     * properties:
     * <ul>
     * <li><tt>felix.auto.deploy.dir</tt> - Specifies the auto-deploy directory
     * from which bundles are automatically deploy at framework startup.
     * The default is the <tt>bundle/</tt> directory of the current directory.
     * </li>
     * <li><tt>felix.auto.deploy.action</tt> - Specifies the auto-deploy actions
     * to be found on bundle JAR files found in the auto-deploy directory.
     * The possible actions are <tt>install</tt>, <tt>update</tt>,
     * <tt>start</tt>, and <tt>uninstall</tt>. If no actions are specified,
     * then the auto-deploy directory is not processed. There is no default
     * value for this property.
     * </li>
     * </ul>
     * </li>
     * <li>Bundle auto-properties - Configuration properties which specify URLs
     * to bundles to install/start:
     * <ul>
     * <li><tt>felix.auto.install.N</tt> - Space-delimited list of bundle
     * URLs to automatically install when the framework is started,
     * where <tt>N</tt> is the start level into which the bundle will be
     * installed (e.g., felix.auto.install.2).
     * </li>
     * <li><tt>felix.auto.start.N</tt> - Space-delimited list of bundle URLs
     * to automatically install and start when the framework is started,
     * where <tt>N</tt> is the start level into which the bundle will be
     * installed (e.g., felix.auto.start.2).
     * </li>
     * </ul>
     * </li>
     * </ul>
     * <p>
     * These properties should be specified in the <tt>config.properties</tt>
     * so that they can be processed by the launcher during the framework
     * startup process.
     * </p>
     *
     * @param args Accepts arguments to set the auto-deploy directory and/or
     * the bundle cache directory.
     * @throws Exception If an error occurs.
     *
     */
    public static void main(String[] args) throws Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parseCommandLineArgs(args);
        Main main = new Main();
        main.start(commandLineArgs);
    }

    public void start(CommandLineArgs commandLineArgs) throws URISyntaxException, MalformedURLException, IOException, MissingPropertyException {
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
        System.out.println("User dir: "+ userDirPath);
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

    /**
     * <p> Loads the properties in the system property file associated with the framework installation into
     * <tt>System.setProperty()</tt>. These properties are not directly used by the framework in anyway. By default, the
     * system property file is located in the <tt>conf/</tt> directory of the Felix installation directory and is called
     * "<tt>system.properties</tt>". The installation directory of Felix is assumed to be the parent directory of the
     * <tt>felix.jar</tt> file as found on the system class path property. The precise file from which to load system
     * properties can be set by initializing the "<tt>felix.system.properties</tt>" system property to an arbitrary URL.
     * </p>
     *
     */
    protected void loadSystemProperties(Path rootDirPath) throws MalformedURLException, IOException {
        Properties props = new Properties();
        loadProperties(props, SYSTEM_PROPERTIES_PROP, rootDirPath, SYSTEM_PROPERTIES_FILE_VALUE);

        for (String propertyName : props.stringPropertyNames()) {
            System.setProperty(propertyName, Util.substVars(props.getProperty(propertyName), propertyName, null, null));
        }
        
    }

    private void loadProperties(Properties props, String systemPropertyName, Path rootDirPath, String propertiesFileName) throws IOException, MalformedURLException {
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

    /**
     * <p> Loads the configuration properties in the configuration property file associated with the framework
     * installation; these properties are accessible to the framework and to bundles and are intended for configuration
     * purposes. By default, the configuration property file is located in the <tt>conf/</tt> directory of the Felix
     * installation directory and is called "<tt>config.properties</tt>". The installation directory of Felix is assumed
     * to be the parent directory of the <tt>felix.jar</tt> file as found on the system class path property. The precise
     * file from which to load configuration properties can be set by initializing the
     * "<tt>felix.config.properties</tt>" system property to an arbitrary URL. </p>
     *
     * @return A <tt>Properties</tt> instance or <tt>null</tt> if there was an error.
     *
     */
    private void loadConfigProperties(Properties configProps, Path rootDirPath) throws MalformedURLException, IOException {
        loadProperties(configProps, CONFIG_PROPERTIES_PROP, rootDirPath, CONFIG_PROPERTIES_FILE_VALUE);
    }

    private void copySystemProperties(Properties configProps) {
        for (String propertyName : System.getProperties().stringPropertyNames()) {
            if (propertyName.startsWith("felix.") || propertyName.startsWith("org.osgi.framework.")) {
                configProps.setProperty(propertyName, System.getProperty(propertyName));
            }
        }
    }

    private Path getInstallDirPath() throws URISyntaxException {
        Class<Main> type = Main.class;
        String jarResourceURIString = type.getResource("/" + type.getName().replace(".", "/") + ".class").toURI().toString();
        int endOfJarPathIndex = jarResourceURIString.indexOf("!/");
        String mainJarURIString = endOfJarPathIndex >= 0 ? jarResourceURIString.substring(0, endOfJarPathIndex) : jarResourceURIString;
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
