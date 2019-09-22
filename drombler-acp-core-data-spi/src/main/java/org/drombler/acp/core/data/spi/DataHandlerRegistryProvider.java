package org.drombler.acp.core.data.spi;

import org.drombler.commons.data.DataHandlerRegistry;

/**
 * A {@link DataHandlerRegistry} provider.<br>
 * <br>
 * This module also provides an implementation registered as OSGi service.
 *
 * @author puce
 */
public interface DataHandlerRegistryProvider {

    /**
     * Gets a {@link DataHandlerRegistry}.
     *
     * @return a DataHandlerRegistry
     */
    DataHandlerRegistry getDataHandlerRegistry();
}
