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
package org.drombler.acp.startup.main.impl;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.startlevel.BundleStartLevel;
import org.osgi.framework.startlevel.FrameworkStartLevel;

public class AutoProcessor {

    /**
     * The property name used for the bundle directory.
     *
     */
    public static final String AUTO_DEPLOY_DIR_PROPERY = "felix.auto.deploy.dir";
    /**
     * The default name used for the bundle directory.
     *
     */
    public static final String AUTO_DEPLOY_DIR_VALUE = "bundle";
    /**
     * The property name used to specify auto-deploy actions.
     *
     */
    public static final String AUTO_DEPLOY_ACTION_PROPERY = "felix.auto.deploy.action";
    /**
     * The property name used to specify auto-deploy start level.
     *
     */
    public static final String AUTO_DEPLOY_STARTLEVEL_PROPERY = "felix.auto.deploy.startlevel";
    /**
     * The name used for the auto-deploy install action.
     *
     */
    public static final String AUTO_DEPLOY_INSTALL_VALUE = "install";
    /**
     * The name used for the auto-deploy start action.
     *
     */
    public static final String AUTO_DEPLOY_START_VALUE = "start";
    /**
     * The name used for the auto-deploy update action.
     *
     */
    public static final String AUTO_DEPLOY_UPDATE_VALUE = "update";
    /**
     * The name used for the auto-deploy uninstall action.
     *
     */
    public static final String AUTO_DEPLOY_UNINSTALL_VALUE = "uninstall";
    /**
     * The property name prefix for the launcher's auto-install property.
     *
     */
    public static final String AUTO_INSTALL_PROP = "felix.auto.install";
    /**
     * The property name prefix for the launcher's auto-start property.
     *
     */
    public static final String AUTO_START_PROP = "felix.auto.start";

    public void process(Framework framework, Map<String, String> configMap, Path installDirPath, Path userDirPath) throws IOException {
        if (configMap == null) {
            configMap = new HashMap<>();
        }
        BundleContext frameworkContext = framework.getBundleContext();
        FrameworkStartLevel fsl = framework.adapt(FrameworkStartLevel.class);
        processAutoDeploy(configMap, frameworkContext, fsl, installDirPath, userDirPath);
        processAutoProperties(configMap, frameworkContext, fsl);
    }

    private void getAutoDeployBundlePaths(Path autoDirPath, final List<Path> jarPaths) throws IOException {
        if (Files.exists(autoDirPath)) {
            Files.walkFileTree(autoDirPath, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.getFileName().toString().endsWith(".jar")) {
                        jarPaths.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    private void processAutoDeploy(Map<String, String> configMap, BundleContext frameworkContext, FrameworkStartLevel fsl,
            Path installDirPath, Path userDirPath) throws IOException {
        Set<BundleAction> bundleActions = getBundleActions(configMap);

        // Perform auto-deploy actions.
        if (!bundleActions.isEmpty()) {
            int startLevel = getStartLevel(fsl, configMap);

            Map<String, Bundle> uninstallBundlesMap = getInstalledBundlesMap(frameworkContext);

            List<Path> jarPaths = getAutoDeployBundlePaths(configMap, installDirPath, userDirPath);

            List<Bundle> startBundleList = new ArrayList<>();
            for (Path jarPath : jarPaths) {
                Bundle b = uninstallBundlesMap.remove(jarPath.toUri().toString());

                try {
                    // If the bundle is not already installed, then install it
                    // if the 'install' action is present.
                    if ((b == null) && bundleActions.contains(BundleAction.INSTALL)) {
                        b = frameworkContext.installBundle(jarPath.toUri().toString());
                    } // If the bundle is already installed, then update it
                    // if the 'update' action is present.
                    else if ((b != null) && bundleActions.contains(BundleAction.UPDATE)) {
                        b.update();
                    }

                    // If we have found and/or successfully installed a bundle,
                    // then add it to the list of bundles to potentially start
                    // and also set its start level accordingly.
                    if ((b != null) && !isFragment(b)) {
                        startBundleList.add(b);
                        setStartLevel(b, startLevel);
                    }
                } catch (BundleException ex) {
                    System.err.println("Auto-deploy install: "
                            + ex + ((ex.getCause() != null) ? " - " + ex.getCause() : ""));
                }
            }

            if (bundleActions.contains(BundleAction.UNINSTALL)) {
                uninstallBundles(uninstallBundlesMap.values());
            }

            if (bundleActions.contains(BundleAction.START)) {
                startBundles(startBundleList);
            }
        }
    }

    private void processAutoProperties(Map<String, String> configMap, BundleContext frameworkContext, FrameworkStartLevel fsl) {
        installAutoInstallBundles(configMap, frameworkContext, fsl);
        startAutoStartBundles(configMap, frameworkContext);

    }

    private void installAutoInstallBundles(Map<String, String> configMap, BundleContext frameworkContext, FrameworkStartLevel fsl) {
        for (String key : configMap.keySet()) {
            key = key.toLowerCase();

            if (key.startsWith(AUTO_INSTALL_PROP) || key.startsWith(AUTO_START_PROP)) {
                int startLevel = getAutoStartLevel(fsl, key);

                StringTokenizer st = new StringTokenizer(configMap.get(key), "\" ", true);
                for (String location = nextLocation(st); location != null; location = nextLocation(st)) {
                    installBundle(location, frameworkContext, startLevel);
                }
            }
        }
    }

    private void startAutoStartBundles(Map<String, String> configMap, BundleContext frameworkContext) {
        for (String key : configMap.keySet()) {
            key = key.toLowerCase();
            if (key.startsWith(AUTO_START_PROP)) {
                StringTokenizer st = new StringTokenizer(configMap.get(key), "\" ", true);
                for (String location = nextLocation(st); location != null; location = nextLocation(st)) {
                    startBundle(frameworkContext, location);
                }
            }
        }
    }

    private int getAutoStartLevel(FrameworkStartLevel fsl, String key) {

        int startLevel = fsl.getInitialBundleStartLevel();
        if (!key.equals(AUTO_INSTALL_PROP) && !key.equals(AUTO_START_PROP)) {
            try {
                startLevel = Integer.parseInt(key.substring(key.lastIndexOf('.') + 1));
            } catch (NumberFormatException ex) {
                System.err.println("Invalid property: " + key);
            }
        }
        return startLevel;
    }

    private void installBundle(String location, BundleContext frameworkContext, int startLevel) {
        try {
            Bundle b = frameworkContext.installBundle(location, null);
            setStartLevel(b, startLevel);
        } catch (Exception ex) {
            System.err.println("Auto-properties install: " + location + " ("
                    + ex + ((ex.getCause() != null) ? " - " + ex.getCause() : "") + ")");
            if (ex.getCause() != null) {
                ex.printStackTrace();
            }
        }
    }

    private void uninstallBundles(Collection<Bundle> bundles) {
        for (Bundle bundle : bundles) {
            if (bundle.getBundleId() != 0) {
                try {
                    bundle.uninstall();
                } catch (BundleException ex) {
                    System.err.println("Auto-deploy uninstall: "
                            + ex + ((ex.getCause() != null) ? " - " + ex.getCause() : ""));
                }
            }
        }
    }

    private void startBundles(List<Bundle> startBundleList) {
        for (Bundle bundle : startBundleList) {
            try {
                bundle.start();
            } catch (Exception ex) {
                System.err.println("Auto-deploy start: "
                        + ex + ((ex.getCause() != null) ? " - " + ex.getCause() : ""));
            }
        }
    }

    private void setStartLevel(Bundle bundle, int startLevel) {
        BundleStartLevel bsl = bundle.adapt(BundleStartLevel.class);
        bsl.setStartLevel(startLevel);
    }

    private void startBundle(BundleContext frameworkContext, String location) {
        // Installing twice just returns the same bundle.
        try {
            Bundle b = frameworkContext.installBundle(location, null);
            if (b != null) {
                b.start();
            }
        } catch (Exception ex) {
            System.err.println("Auto-properties start: " + location + " ("
                    + ex + ((ex.getCause() != null) ? " - " + ex.getCause() : "") + ")");
        }
    }

    private int getStartLevel(FrameworkStartLevel fsl, Map<String, String> configMap) {
        int startLevel = fsl.getInitialBundleStartLevel();
        if (configMap.get(AUTO_DEPLOY_STARTLEVEL_PROPERY) != null) {
            try {
                startLevel = Integer.parseInt(
                        configMap.get(AUTO_DEPLOY_STARTLEVEL_PROPERY).toString());
            } catch (NumberFormatException ex) {
                // Ignore and keep default level.
            }
        }
        return startLevel;
    }

    private Set<BundleAction> getBundleActions(Map<String, String> configMap) {
        String action = configMap.get(AUTO_DEPLOY_ACTION_PROPERY);
        if (action == null) {
            action = "";
        }
        Set<BundleAction> bundleActions = EnumSet.noneOf(BundleAction.class);
        StringTokenizer st = new StringTokenizer(action, ",");
        while (st.hasMoreTokens()) {
            BundleAction bundleAction = BundleAction.getBundleAction(st.nextToken().trim().toLowerCase());
            if (bundleAction != null) {
                bundleActions.add(bundleAction);
            }
        }
        return bundleActions;
    }

    private List<Path> getAutoDeployBundlePaths(Map<String, String> configMap, Path installDirPath, Path userDirPath) throws IOException {
        String autoDir = getAutoDeployDir(configMap);
        final List<Path> jarPaths = new ArrayList<>();

        getAutoDeployBundlePaths(installDirPath.resolve(autoDir), jarPaths);
        getAutoDeployBundlePaths(userDirPath.resolve(autoDir), jarPaths);

        Collections.sort(jarPaths); // TODO: needed?
        return jarPaths;
    }

    private String getAutoDeployDir(Map<String, String> configMap) {
        String autoDir = configMap.get(AUTO_DEPLOY_DIR_PROPERY);
        if (autoDir == null) {
            autoDir = AUTO_DEPLOY_DIR_VALUE;
        }
        return autoDir;
    }

    private Map<String, Bundle> getInstalledBundlesMap(BundleContext frameworkContext) {
        Bundle[] bundles = frameworkContext.getBundles();
        Map<String, Bundle> installedBundleMap = new HashMap<>(bundles.length);
        for (Bundle bundle : bundles) {
            installedBundleMap.put(bundle.getLocation(), bundle);
        }
        return installedBundleMap;


    }

    private String nextLocation(StringTokenizer st) {
        String retVal = null;

        if (st.countTokens() > 0) {
            String tokenList = "\" ";
            StringBuffer tokBuf = new StringBuffer(10);
            boolean inQuote = false;
            boolean tokStarted = false;
            boolean exit = false;
            while ((st.hasMoreTokens()) && (!exit)) {
                String tok = st.nextToken(tokenList);
                switch (tok) {
                    case "\"":
                        inQuote = !inQuote;
                        if (inQuote) {
                            tokenList = "\"";
                        } else {
                            tokenList = "\" ";
                        }
                        break;
                    case " ":
                        if (tokStarted) {
                            retVal = tokBuf.toString();
                            tokStarted = false;
                            tokBuf = new StringBuffer(10);
                            exit = true;
                        }
                        break;
                    default:
                        tokStarted = true;
                        tokBuf.append(tok.trim());
                        break;
                }
            }

            // Handle case where end of token stream and
            // still got data
            if ((!exit) && (tokStarted)) {
                retVal = tokBuf.toString();
            }
        }

        return retVal;
    }

    private boolean isFragment(Bundle bundle) {
        return bundle.getHeaders().get(Constants.FRAGMENT_HOST) != null;
    }
}