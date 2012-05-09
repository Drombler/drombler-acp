/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.application;

import java.util.concurrent.Executor;

/**
 *
 * @author puce
 */
public interface ApplicationExecutorProvider {

    Executor getApplicationExecutor();
}
