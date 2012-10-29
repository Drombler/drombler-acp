/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.application;

import java.util.concurrent.Executor;

/**
 *
 * @author puce
 */
public interface ApplicationExecutorProvider {

    Executor getApplicationExecutor();
}
