package org.drombler.acp.core.data.spi;

import java.util.EventListener;

/**
 *
 * @author puce
 */
public interface FileExtensionListener extends EventListener {

    void fileExtensionAdded(FileExtensionEvent event);

    void fileExtensionRemoved(FileExtensionEvent event);
}
