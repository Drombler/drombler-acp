/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (GitHub user: puce77).
 * Copyright 2016 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.data.spi.impl;

import org.drombler.acp.core.data.jaxb.DataHandlersType;
import org.drombler.acp.core.data.spi.DataHandlerDescriptorRegistryProvider;
import org.drombler.commons.data.AbstractDataHandlerDescriptor;
import org.drombler.commons.data.DataHandlerDescriptorRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author puce
 */
public abstract class AbstractDataHandlerHandler<D extends AbstractDataHandlerDescriptor<?>> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDataHandlerHandler.class);

    private final List<D> unresolvedDataHandlerDescriptors = new ArrayList<>();

    @Reference
    protected DataHandlerDescriptorRegistryProvider dataHandlerDescriptorRegistryProvider;

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindDataHandlersType(ServiceReference<DataHandlersType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        DataHandlersType dataHandlersType = context.getService(serviceReference);
        registerDataHandlers(dataHandlersType, context);
    }

    protected void unbindDataHandlersType(DataHandlersType documentHandlersType) {
        // TODO
    }

    protected void activate(ComponentContext context) {
        resolveUnresolvedDocumentHandlerDescriptors();
    }

    protected void deactivate(ComponentContext context) {
    }

    protected abstract void registerDataHandlers(DataHandlersType dataHandlersType, BundleContext context);

    public DataHandlerDescriptorRegistry getDataHandlerDescriptorRegistry() {
        return dataHandlerDescriptorRegistryProvider.getDataHandlerDescriptorRegistry();
    }

    private void resolveUnresolvedDocumentHandlerDescriptors() {
        unresolvedDataHandlerDescriptors.forEach(this::resolveDataHandlerDescriptor);
    }

    protected void resolveDataHandlerDescriptor(D handlerDescriptor) {
        LOG.debug("Resolving {}...", handlerDescriptor);
        if (isInitialized()) {
            resolveDataHandlerDescriptorInitialized(handlerDescriptor);
            LOG.debug("Resolved {}...", handlerDescriptor);
        } else {
            unresolvedDataHandlerDescriptors.add(handlerDescriptor);
        }
    }

    protected boolean isInitialized() {
        return dataHandlerDescriptorRegistryProvider != null;
    }

    protected void resolveDataHandlerDescriptorInitialized(D handlerDescriptor) {
        getDataHandlerDescriptorRegistry().registerDataHandlerDescriptor(handlerDescriptor);
    }
}
