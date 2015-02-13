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
package org.drombler.acp.core.application;

/**
 * The Extension Point interface. Extensions can be registered in the application.xml file.
 *
 * Note: More method might be added in the future!
 *
 * @author puce
 * @param <T> the type of the JAXB root class of this Extension Point.
 */
public interface ExtensionPoint<T> {

    Class<T> getJAXBRootClass();

//    Collection<T> getExtensions();
}
