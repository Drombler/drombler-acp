/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.lib.util.context;

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
