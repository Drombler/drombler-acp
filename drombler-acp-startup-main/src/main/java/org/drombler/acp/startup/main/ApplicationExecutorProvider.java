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

import java.util.concurrent.Executor;

/**
 * A provider which provides an {@link Executor}, which makes sure a given command gets executed on the GUI-toolkit
 * specific application thread.
 *
 * @author puce
 */
@FunctionalInterface
public interface ApplicationExecutorProvider {

    /**
     * Gets an {@link Executor}, which makes sure a given command gets executed on the GUI-toolkit specific application
     * thread
     *
     * @return an {@link Executor}, which makes sure a given command gets executed on the GUI-toolkit specific
     * application thread
     */
    Executor getApplicationExecutor();
}
