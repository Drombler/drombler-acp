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

import org.drombler.acp.core.data.jaxb.BusinessObjectHandlerType;
import org.drombler.acp.core.data.jaxb.DataHandlersType;
import org.drombler.commons.data.BusinessObjectHandlerDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class BusinessObjectHandlerHandler extends AbstractDataHandlerHandler<BusinessObjectHandlerDescriptor<?>> {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessObjectHandlerHandler.class);

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected void bindBusinessObjectHandlerDescriptor(BusinessObjectHandlerDescriptor<?> handlerDescriptor) {
        resolveDataHandlerDescriptor(handlerDescriptor);
    }

    protected void unbindBusinessObjectHandlerDescriptor(BusinessObjectHandlerDescriptor<?> handlerDescriptor) {
    }

    @Activate
    @Override
    protected void activate(ComponentContext context) {
        super.activate(context);
    }

    @Deactivate
    @Override
    protected void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    @Override
    protected void registerDataHandlers(DataHandlersType dataHandlersType, BundleContext context) {
        dataHandlersType.getBusinessObjectHandler().forEach(businessObjectHandler
                -> registerBusinessObjectHandler(businessObjectHandler, context));
    }

    private void registerBusinessObjectHandler(BusinessObjectHandlerType businessObjectHandler, BundleContext context) {
        try {
            // TODO: register BusinessObjectHandlerDescriptor as service?
            BusinessObjectHandlerDescriptor<?> businessObjectHandlerDescriptor = DataHandlerUtils.createBusinessObjectHandlerDescriptor(
                    businessObjectHandler, context.getBundle());
            resolveDataHandlerDescriptor(businessObjectHandlerDescriptor);
        } catch (ClassNotFoundException | RuntimeException ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }



}
