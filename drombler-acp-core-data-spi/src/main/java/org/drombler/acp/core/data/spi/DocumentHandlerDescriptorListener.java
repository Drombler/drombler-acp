package org.drombler.acp.core.data.spi;

import java.util.EventListener;

/**
 *
 * @author puce
 */
public interface DocumentHandlerDescriptorListener extends EventListener {

    void documentHandlerDescriptorAdded(DocumentHandlerDescriptorEvent<?> event);

    void documentHandlerDescriptorRemoved(DocumentHandlerDescriptorEvent<?> event);
}
