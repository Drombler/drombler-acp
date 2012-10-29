/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.startup.main.impl;

/**
 *
 * @author puce
 */
public class CommandLineArgs {

    /**
     * Switch for specifying the user directory.
     *
     */
    public static final String USER_DIR_SWITCH = "--userDir";
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
        System.out.println("Usage: [--userDir <user-directory>]");
        System.exit(0);
    }

    /**
     * @return the userDir
     */
    public String getUserDir() {
        return userDir;
    }

}
