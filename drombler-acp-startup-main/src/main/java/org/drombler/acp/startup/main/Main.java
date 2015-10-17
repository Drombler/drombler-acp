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
import org.drombler.acp.startup.main.impl.CommandLineArgs;
import org.drombler.acp.startup.main.impl.osgi.OSGiStarter;
import org.osgi.framework.BundleException;

public class Main {

//    private static final Logger LOG = LoggerFactory.getLogger(Main.class); // TODO: outside OSGi Framework...?



    public static void main(String[] args) throws URISyntaxException, IOException,
            MissingPropertyException, BundleException, InterruptedException {
        CommandLineArgs commandLineArgs = CommandLineArgs.parseCommandLineArgs(args);
        Main main = new Main(commandLineArgs);
        main.init();
        main.start();
    }

    private final OSGiStarter osgiStarter;

    public Main(CommandLineArgs commandLineArgs) throws URISyntaxException, IOException, MissingPropertyException {
        ApplicationConfiguration configuration = new ApplicationConfiguration(commandLineArgs);
        osgiStarter = new OSGiStarter(configuration);
    }

    public void init() throws BundleException, IOException {
        osgiStarter.init();
    }

    public void start() throws BundleException, InterruptedException {
        osgiStarter.start();
    }

}
