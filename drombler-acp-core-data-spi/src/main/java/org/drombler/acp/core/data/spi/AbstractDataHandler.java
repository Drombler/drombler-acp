package org.drombler.acp.core.data.spi;

import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.data.DataCapabilityProvider;
import org.drombler.commons.context.Context;
import org.drombler.commons.context.LocalContextProvider;
import org.drombler.commons.context.SimpleContext;
import org.drombler.commons.context.SimpleContextContent;
import org.osgi.util.tracker.ServiceTracker;

/**
 * An abstract data handler. It observes registered {@link DataCapabilityProvider}s and adds the found data capabilities to it's local context.
 *
 * You can use this class as a base class for your own data handler implementations.
 *
 * @see AbstractDocumentHandler
 * @author puce
 */
public abstract class AbstractDataHandler implements AutoCloseable, LocalContextProvider {

    private final SimpleContextContent contextContent = new SimpleContextContent();
    private final Context localContext = new SimpleContext(contextContent);
    private final ServiceTracker<DataCapabilityProvider, DataCapabilityProvider> dataCapabilityProviderTracker;

    /**
     * Creates a new instance of this class.
     */
    public AbstractDataHandler() {
        this.dataCapabilityProviderTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DataCapabilityProvider.class, this::addDataCapabilityProvider, this::removeDataCapabilityProvider);
        dataCapabilityProviderTracker.open(true);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Context getLocalContext() {
        return localContext;
    }

    private void addDataCapabilityProvider(DataCapabilityProvider<?> dataCapabilityProvider) {
        contextContent.add(dataCapabilityProvider.getDataCapability(this));
    }

    private void removeDataCapabilityProvider(DataCapabilityProvider<?> dataCapabilityProvider) {
        contextContent.remove(dataCapabilityProvider.getDataCapability(this));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void close() {
        dataCapabilityProviderTracker.close();
    }
}
