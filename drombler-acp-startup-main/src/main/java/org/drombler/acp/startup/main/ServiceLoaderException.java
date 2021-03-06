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
 * Copyright 2015 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.startup.main;

/**
 * A service loader exception.
 *
 * @author puce
 */
// TODO: use org.softsmithy.lib.util.ServiceLoaderException from SoftSmithy? startup dependency?
public class ServiceLoaderException extends RuntimeException {
    private static final long serialVersionUID = -326132137354684616L;

    /**
     * Creates a new instance of this class.
     */
    public ServiceLoaderException() {
    }

    /**
     * Creates a new instance of this class.
     *
     * @param message the message
     */
    public ServiceLoaderException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param message the message
     * @param cause the cause
     */
    public ServiceLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance of this class.
     *
     * @param cause the cause
     */
    public ServiceLoaderException(Throwable cause) {
        super(cause);
    }

}
