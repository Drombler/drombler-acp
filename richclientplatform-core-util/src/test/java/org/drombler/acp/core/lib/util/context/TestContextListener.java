/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.lib.util.context;

import org.drombler.acp.core.lib.util.context.ContextListener;
import org.drombler.acp.core.lib.util.context.ContextEvent;

/**
 *
 * @author puce
 */
public class TestContextListener implements ContextListener {

    private boolean contextChanged = false;

    @Override
    public void contextChanged(ContextEvent event) {
        contextChanged = true;
    }

    public boolean isContextChanged() {
        return contextChanged;
    }

    public void reset() {
        contextChanged = false;
    }
}
