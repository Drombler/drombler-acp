package org.drombler.acp.core.context;

import org.drombler.commons.context.ContextManager;

/**
 * A {@link ContextManager} provider registered as OSGi service.<br>
 * <br>
 * This module also provides a registered implementation.
 */
public interface ContextManagerProvider {

    /**
     * Gets the {@link ContextManager}.
     *
     * @return the context manager
     */
    ContextManager getContextManager();
}
