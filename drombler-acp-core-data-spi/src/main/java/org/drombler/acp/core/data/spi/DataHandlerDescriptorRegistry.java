package org.drombler.acp.core.data.spi;

public interface DataHandlerDescriptorRegistry {

    void registerDataHandlerDescriptor(AbstractDataHandlerDescriptor<?> dataHandlerDescriptor);

    void unregisterDataHandlerDescriptor(AbstractDataHandlerDescriptor<?> documentHandlerDescriptor);

    AbstractDataHandlerDescriptor<?> getDataHandlerDescriptor(Object dataHandler);

    void registerDataHandlerDescriptorListener(DataHandlerDescriptorListener listener);

    void unregisterDataHandlerDescriptorListener(DataHandlerDescriptorListener listener);

}
