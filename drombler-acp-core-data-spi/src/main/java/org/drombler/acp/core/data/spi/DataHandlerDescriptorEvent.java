package org.drombler.acp.core.data.spi;

import java.util.EventObject;

/**
 *
 * @author puce
 */
public class DataHandlerDescriptorEvent<D> extends EventObject {

    private static final long serialVersionUID = -2640012533238693088L;

    private final AbstractDataHandlerDescriptor<D> dataHandlerDescriptor;

    public DataHandlerDescriptorEvent(DataHandlerDescriptorRegistry source, AbstractDataHandlerDescriptor<D> dataHandlerDescriptor1) {
        super(source);
        this.dataHandlerDescriptor = dataHandlerDescriptor1;
    }

    /**
     * @return the dataHandlerDescriptor
     */
    public AbstractDataHandlerDescriptor<D> getDataHandlerDescriptor() {
        return dataHandlerDescriptor;
    }

}
