/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.startup.main;

/**
 *
 * @author puce
 */
public class CommandLineArgs {
    /**
     * Switch for specifying bundle directory.
     *
     */
    public static final String BUNDLE_DIR_SWITCH = "-b";
    
    private final String bundleDir;
    private final String cacheDir;

    private CommandLineArgs(String bundleDir, String cacheDir) {
        this.bundleDir = bundleDir;
        this.cacheDir = cacheDir;
    }

    public static CommandLineArgs parseCommandLineArgs(String[] args){
        // Look for bundle directory and/or cache directory.
        // We support at most one argument, which is the bundle
        // cache directory.
        String bundleDir = null;
        String cacheDir = null;
        boolean expectBundleDir = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(BUNDLE_DIR_SWITCH)) {
                expectBundleDir = true;
            } else if (expectBundleDir) {
                bundleDir = args[i];
                expectBundleDir = false;
            } else {
                cacheDir = args[i];
            }
        }

        if ((args.length > 3) || (expectBundleDir && bundleDir == null)) {
            System.out.println("Usage: [-b <bundle-deploy-dir>] [<bundle-cache-dir>]");
            System.exit(0);
        }
        
        return new CommandLineArgs(bundleDir, cacheDir);
    }
    /**
     * @return the bundleDir
     */
    public String getBundleDir() {
        return bundleDir;
    }

    /**
     * @return the cacheDir
     */
    public String getCacheDir() {
        return cacheDir;
    }
    
    
}
