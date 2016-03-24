package org.drombler.acp.core.data;

/**
 *
 * @author puce
 */
public interface DataCapabilityProvider<T> {

    T getDataCapability(Object data);
}
