/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking.impl;

import org.drombler.acp.core.docking.impl.DockingAreaAnnotationProcessor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author puce
 */
public class DockingAreaAnnotationProcessorTest {

    private DockingAreaAnnotationProcessor processor;


    @Before
    public void setUp() {
        processor = new DockingAreaAnnotationProcessor();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testClassLoading() {
        // Test class loading: tests if all mappings are provided
    }
}
