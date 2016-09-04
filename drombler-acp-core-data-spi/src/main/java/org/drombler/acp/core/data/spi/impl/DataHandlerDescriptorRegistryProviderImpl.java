package org.drombler.acp.core.data.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.data.spi.DataHandlerDescriptorRegistryProvider;
import org.drombler.commons.data.DataHandlerDescriptorRegistry;
import org.drombler.commons.data.SimpleDataHandlerDescriptorRegistry;

/**
 *
 *
 * @author puce
 */
@Component
@Service
public class DataHandlerDescriptorRegistryProviderImpl implements DataHandlerDescriptorRegistryProvider {

    private final DataHandlerDescriptorRegistry dataHandlerDescriptorRegistry = new SimpleDataHandlerDescriptorRegistry();

    @Override
    public DataHandlerDescriptorRegistry getDataHandlerDescriptorRegistry() {
        return dataHandlerDescriptorRegistry;
    }

}
