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

/**
 *
 * @author puce
 */
public class CommandLineArgs {

    /**
     * Switch for specifying the user directory.
     *
     */
    public static final String USER_DIR_SWITCH = "--userdir";
    private final String userDir;

    private CommandLineArgs(String userDir) {
        this.userDir = userDir;
    }

    public static CommandLineArgs parseCommandLineArgs(String[] args) {
        String userDir = null;
        boolean expectUserDir = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(USER_DIR_SWITCH)) {
                expectUserDir = true;
            } else if (expectUserDir) {
                userDir = args[i];
                expectUserDir = false;
            } else {
                wrongArguments();
            }
        }

        if ((args.length > 2) || (expectUserDir && userDir == null)) {
            wrongArguments();
        }

        return new CommandLineArgs(userDir);
    }

    private static void wrongArguments() {
        System.out.println("Usage: [--userdir <user-directory>]");
        System.exit(0);
    }

    /**
     * @return the userDir
     */
    public String getUserDir() {
        return userDir;
    }

}
