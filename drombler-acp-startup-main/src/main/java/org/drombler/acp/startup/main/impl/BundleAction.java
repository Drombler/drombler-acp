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
