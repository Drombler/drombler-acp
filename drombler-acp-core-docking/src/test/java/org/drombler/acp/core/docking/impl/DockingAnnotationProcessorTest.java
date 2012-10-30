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
package org.drombler.acp.core.docking.impl;

import org.drombler.acp.core.docking.impl.DockingAnnotationProcessor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author puce
 */
public class DockingAnnotationProcessorTest {

    private DockingAnnotationProcessor processor;

    public DockingAnnotationProcessorTest() {
    }

    @Before
    public void setUp() {
        processor = new DockingAnnotationProcessor();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testClassLoading() {
        // Test class loading: tests if all mappings are provided
    }
}
