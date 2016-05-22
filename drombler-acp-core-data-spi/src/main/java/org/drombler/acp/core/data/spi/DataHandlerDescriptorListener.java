package org.drombler.acp.core.data.spi;

import java.util.EventListener;

/**
 *
 * @author puce
 */
public interface DataHandlerDescriptorListener extends EventListener {

    void dataHandlerDescriptorAdded(DataHandlerDescriptorEvent<?> event);

    void dataHandlerDescriptorRemoved(DataHandlerDescriptorEvent<?> event);
}
