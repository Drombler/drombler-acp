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
import org.drombler.acp.startup.main.impl.osgi.OSGiStarter;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;

public class DromblerACPStarter {

//    private static final Logger LOG = LoggerFactory.getLogger(DromblerACPStarter.class); // TODO: outside OSGi Framework...?



    public static void main(String[] args) throws URISyntaxException, IOException,
            MissingPropertyException, BundleException, InterruptedException {
        CommandLineArgs commandLineArgs = CommandLineArgs.parseCommandLineArgs(args);
        DromblerACPStarter main = new DromblerACPStarter(new DromblerACPConfiguration(commandLineArgs));
        main.init();
        main.startAndWait();
    }

    private final OSGiStarter osgiStarter;

    public DromblerACPStarter(DromblerACPConfiguration configuration){
        osgiStarter = new OSGiStarter(configuration);
    }

    public void init() throws BundleException, IOException {
        osgiStarter.init();
    }

    public void startAndWait() throws BundleException, InterruptedException {
        osgiStarter.startAndWait();
    }

    public void stop() throws BundleException, InterruptedException {
        osgiStarter.stop();
    }

    public Framework getFramework() {
        return osgiStarter.getFramework();
    }
}
