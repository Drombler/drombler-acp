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
 * Copyright 2016 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.startup.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import org.drombler.acp.startup.main.impl.AdditionalArgumentsProviderImpl;
import org.drombler.acp.startup.main.impl.osgi.OSGiStarter;
import org.drombler.commons.client.startup.main.ApplicationInstanceListener;
import org.drombler.commons.client.startup.main.cli.CommandLineArgs;
import org.drombler.commons.client.startup.main.DromblerClientStarter;
import org.drombler.commons.client.startup.main.MissingPropertyException;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.launch.Framework;

public class DromblerACPStarter<T extends DromblerACPConfiguration> extends DromblerClientStarter<T> {

//    private static final Logger LOG = LoggerFactory.getLogger(DromblerACPStarter.class); // TODO: outside OSGi Framework...?
    public static void main(String[] args) throws URISyntaxException, IOException,
            MissingPropertyException, BundleException, InterruptedException, Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parseCommandLineArgs(args);
        DromblerACPStarter<DromblerACPConfiguration> main = new DromblerACPStarter<>(new DromblerACPConfiguration(commandLineArgs));
        if (main.init()) {
            main.start();
        }
    }

    private final OSGiStarter osgiStarter;

    /**
     *
     * @param configuration
     */
    public DromblerACPStarter(T configuration) {
        super(configuration);
        this.osgiStarter = new OSGiStarter(configuration, true);
        addAdditionalStarters(osgiStarter);
    }

    @Override
    public void start() {
        super.start();
        getFramework().getBundleContext().
                addFrameworkListener((FrameworkEvent event) -> {
                    if (event.getType() == FrameworkEvent.STARTED) {
                        fireAdditionalArguments(getConfiguration().getCommandLineArgs().getAdditionalArguments());
                    }
                });
    }

    private void fireAdditionalArguments(List<String> additionalArguments) {
        osgiStarter.getFramework().getBundleContext().registerService(AdditionalArgumentsProvider.class,
                new AdditionalArgumentsProviderImpl(additionalArguments), null);

    }

    public final Framework getFramework() {
        return osgiStarter.getFramework();
    }

    @Override
    protected ApplicationInstanceListener getApplicationInstanceListener() {
        return this::fireAdditionalArguments;
    }

}
