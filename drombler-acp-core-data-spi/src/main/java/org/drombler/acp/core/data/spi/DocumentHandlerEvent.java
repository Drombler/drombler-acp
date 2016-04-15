package org.drombler.acp.core.data.spi;

import java.util.EventObject;

/**
 *
 * @author puce
 */
public class DocumentHandlerEvent<D> extends EventObject {

    private static final long serialVersionUID = 1837696044825618602L;

    private final DocumentHandlerDescriptor<D> documentHandlerDescriptor;

    public DocumentHandlerEvent(DocumentHandlerDescriptorRegistry source, DocumentHandlerDescriptor<D> documentHandlerDescriptor) {
        super(source);
        this.documentHandlerDescriptor = documentHandlerDescriptor;
    }

    /**
     * @return the documentHandlerDescriptor
     */
    public DocumentHandlerDescriptor<D> getDocumentHandlerDescriptor() {
        return documentHandlerDescriptor;
    }

}
