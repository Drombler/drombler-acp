package org.drombler.acp.core.context.impl;

import org.drombler.acp.core.context.ContextManagerProvider;
import org.drombler.commons.context.ActiveContextProvider;
import org.drombler.commons.context.ApplicationContextProvider;
import org.drombler.commons.context.Context;
import org.drombler.commons.context.ContextManager;
import org.osgi.service.component.annotations.Component;

/**
 *
 * @author puce
 */
@Component
public class SimpleContextManagerProvider implements ContextManagerProvider, ActiveContextProvider, ApplicationContextProvider {

    private final ContextManager contextManager = new ContextManager();

    @Override
    public ContextManager getContextManager() {
        return contextManager;
    }

    @Override
    public Context getActiveContext() {
        return contextManager.getActiveContext();
    }

    @Override
    public Context getApplicationContext() {
        return contextManager.getApplicationContext();
    }
}
