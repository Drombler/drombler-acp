/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.lib.util;

import java.util.EventListener;

/**
 *
 * @author puce
 */
public interface ContextListener extends EventListener {

    void contextChanged(ContextEvent event);
}
