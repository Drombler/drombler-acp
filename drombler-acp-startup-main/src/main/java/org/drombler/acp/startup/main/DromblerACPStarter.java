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
package org.drombler.acp.startup.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.drombler.acp.startup.main.impl.AdditionalArgumentsProviderImpl;
import org.drombler.acp.startup.main.impl.BootServiceStarter;
import org.drombler.acp.startup.main.impl.osgi.OSGiStarter;
import org.drombler.acp.startup.main.impl.singleinstance.SingleInstanceStarter;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;

public class DromblerACPStarter {

//    private static final Logger LOG = LoggerFactory.getLogger(DromblerACPStarter.class); // TODO: outside OSGi Framework...?
    public static void main(String[] args) throws URISyntaxException, IOException,
            MissingPropertyException, BundleException, InterruptedException, Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parseCommandLineArgs(args);
        DromblerACPStarter main = new DromblerACPStarter(new DromblerACPConfiguration(commandLineArgs));
        main.init();
        main.start();
    }

    private final OSGiStarter osgiStarter;
    private final SingleInstanceStarter singleInstanceStarter;
    private final List<BootServiceStarter> starters;
    private final DromblerACPConfiguration configuration;
    private boolean stopped = false;

    /**
     *
     * @param configuration
     */
    public DromblerACPStarter(DromblerACPConfiguration configuration) {
        this.configuration = configuration;
        this.osgiStarter = new OSGiStarter(configuration, true);
        this.singleInstanceStarter = new SingleInstanceStarter(configuration);
        this.starters = Arrays.asList(singleInstanceStarter, osgiStarter).stream()
                .filter(BootServiceStarter::isActive)
                .collect(Collectors.toList());
    }

    public boolean init() throws Exception {
        boolean initialized = true;
        for (BootServiceStarter starter : starters) {
            if (!starter.init()) {
                initialized = false;
                break;
            }
        }
        return initialized;
    }

    public void start() {
        starters.stream().
                map(starter -> Executors.defaultThreadFactory().newThread(() -> {
                    try {
                        registerShutdownHook(starter);
                        starter.startAndWait();
                    } catch (Exception ex) {
                        logError(ex);
//                } finally {
//                    try {
//                        DromblerFXApplication.this.stopStarter();
//                    } catch (BundleException | InterruptedException ex) {
//                        logError(ex);
//                    }
                    }
                })).
                forEach(Thread::start);
        fireAdditionalArguments(configuration.getCommandLineArgs().getAdditionalArguments());
        if (singleInstanceStarter.isActive()) {
            singleInstanceStarter.setApplicationInstanceListener(this::fireAdditionalArguments);
        }
    }

    private void registerShutdownHook(BootServiceStarter starter) {
        Runtime.getRuntime().addShutdownHook(new Thread(starter.getName() + " Shutdown Hook") {

            @Override
            public void run() {
                try {
                    if (starter.isRunning()) {
                        starter.stop();
                    }
                } catch (Exception ex) {
                    System.err.println("Error stopping starter " + starter.getName() + ": " + ex);
                }
            }
        });
    }

    public synchronized void stop() {
        if (!stopped) {
            stopped = true;
            starters.stream()
                    .forEach(starter -> {
                        try {
                            starter.stop();
                        } catch (Exception ex) {
                            logError(ex);
                        }
                    });
        }

    }

    private void fireAdditionalArguments(List<String> additionalArguments) {
        osgiStarter.getFramework().getBundleContext().registerService(AdditionalArgumentsProvider.class,
                new AdditionalArgumentsProviderImpl(additionalArguments), null);

    }

    public Framework getFramework() {
        return osgiStarter.getFramework();
    }

    private void logInfo(String messageFormat, Object... arguments) {
        // TODO: replace with SLF4J Logger once available on classpath
        // Note: the message format is different!
        System.out.println(MessageFormat.format(messageFormat, arguments));
    }

    private void logError(Exception ex) {
        // TODO: replace with SLF4J Logger once available on classpath
        // Note: the message format is different!
        ex.printStackTrace();
    }

}
