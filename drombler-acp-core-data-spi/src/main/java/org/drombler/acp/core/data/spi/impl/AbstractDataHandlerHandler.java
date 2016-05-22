package org.drombler.acp.core.data.spi.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.drombler.acp.core.data.jaxb.DataHandlersType;
import org.drombler.acp.core.data.spi.AbstractDataHandlerDescriptor;
import org.drombler.acp.core.data.spi.DataHandlerDescriptorRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

/**
 *
 * @author puce
 */
@References({
    @Reference(name = "dataHandlersType", referenceInterface = DataHandlersType.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
})
public abstract class AbstractDataHandlerHandler<T, D extends AbstractDataHandlerDescriptor<T>> {

    private final List<D> unresolvedDataHandlerDescriptors = new ArrayList<>();

    @Reference
    private DataHandlerDescriptorRegistry dataHandlerDescriptorRegistry;

    protected void bindDataHandlersType(ServiceReference<DataHandlersType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        DataHandlersType dataHandlersType = context.getService(serviceReference);
        registerDataHandlers(dataHandlersType, context);
    }

    protected void unbindDataHandlersType(DataHandlersType documentHandlersType) {
        // TODO
    }

    protected void bindDataHandlerDescriptorRegistry(DataHandlerDescriptorRegistry dataHandlerDescriptorRegistry) {
        this.dataHandlerDescriptorRegistry = dataHandlerDescriptorRegistry;
    }

    protected void unbindDataHandlerDescriptorRegistry(DataHandlerDescriptorRegistry dataHandlerDescriptorRegistry) {
        this.dataHandlerDescriptorRegistry = null;
    }

    protected void activate(ComponentContext context) {
        resolveUnresolvedDocumentHandlerDescriptors();
    }

    protected void deactivate(ComponentContext context) {
    }

    protected abstract void registerDataHandlers(DataHandlersType dataHandlersType, BundleContext context);

    public DataHandlerDescriptorRegistry getDataHandlerDescriptorRegistry() {
        return dataHandlerDescriptorRegistry;
    }

    private void resolveUnresolvedDocumentHandlerDescriptors() {
        unresolvedDataHandlerDescriptors.forEach(this::resolveDataHandlerDescriptor);
    }

    protected void resolveDataHandlerDescriptor(D handlerDescriptor) {
        if (isInitialized()) {
            resolveDataHandlerDescriptorInitialized(handlerDescriptor);
        } else {
            unresolvedDataHandlerDescriptors.add(handlerDescriptor);
        }
    }

    protected boolean isInitialized() {
        return dataHandlerDescriptorRegistry != null;
    }

    protected void resolveDataHandlerDescriptorInitialized(D handlerDescriptor) {
        getDataHandlerDescriptorRegistry().registerDataHandlerDescriptor(handlerDescriptor);
    }
}
