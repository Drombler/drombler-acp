/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
