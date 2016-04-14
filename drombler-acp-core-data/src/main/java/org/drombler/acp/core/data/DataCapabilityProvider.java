package org.drombler.acp.core.data;

/**
 * A data capability provider.
 *
 * @author puce
 * @param <T> the data capability type
 */
public interface DataCapabilityProvider<T> {

    /**
     * Gets a capability for the given data.
     *
     * @param data the data
     * @return a data capability
     */
    T getDataCapability(Object data);
}
