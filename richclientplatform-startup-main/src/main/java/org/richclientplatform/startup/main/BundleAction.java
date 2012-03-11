/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.startup.main;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author puce
 */
public enum BundleAction {

    INSTALL("install"),
    START("start"),
    UPDATE("update"),
    UNINSTALL("uninstall");
    private static final Map<String, BundleAction> bundleActions = new HashMap<>(BundleAction.values().length);

    static {
        for (BundleAction bundleAction : values()) {
            bundleActions.put(bundleAction.id, bundleAction);
        }
    }
    private final String id;

    private BundleAction(String id) {
        this.id = id;
    }

    public static BundleAction getBundleAction(String bundleActionId) {
        return bundleActions.get(bundleActionId);
    }
}
