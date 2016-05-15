package org.drombler.acp.core.data.spi;

import org.drombler.acp.core.data.jaxb.BusinessObjectHandlerType;
import org.osgi.framework.Bundle;

public class BusinessObjectHandlerDescriptor<D> extends AbstractDataHandlerDescriptor<D> {

    public static BusinessObjectHandlerDescriptor createBusinessObjectHandlerDescriptor(BusinessObjectHandlerType businessObjectHandler, Bundle bundle) throws ClassNotFoundException {
        Class<?> handlerClass = loadHandlerClass(businessObjectHandler, bundle);
        return createBusinessObjectHandlerDescriptor(businessObjectHandler, handlerClass);
    }

    private static <D> BusinessObjectHandlerDescriptor<D> createBusinessObjectHandlerDescriptor(BusinessObjectHandlerType businessObjectHandler,
            Class<D> handlerClass) {
        BusinessObjectHandlerDescriptor<D> descriptor = new BusinessObjectHandlerDescriptor<>();
        configureDataHandlerDescriptor(descriptor, businessObjectHandler, handlerClass);
        return descriptor;
    }

}
