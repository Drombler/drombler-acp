package org.drombler.acp.core.data.spi;

import java.util.EventListener;

/**
 *
 * @author puce
 */
public interface DocumentHandlerListener extends EventListener {

    void documentHandlerAdded(DocumentHandlerEvent<?> event);

    void documentHandlerRemoved(DocumentHandlerEvent<?> event);
}
