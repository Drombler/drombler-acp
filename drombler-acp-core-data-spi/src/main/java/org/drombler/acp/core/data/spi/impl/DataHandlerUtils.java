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

import org.apache.commons.lang3.StringUtils;
import static org.drombler.acp.core.commons.util.BundleUtils.loadClass;
import org.drombler.acp.core.data.jaxb.BusinessObjectHandlerType;
import org.drombler.acp.core.data.jaxb.DataHandlerType;
import org.drombler.acp.core.data.jaxb.DocumentHandlerType;
import org.drombler.commons.data.AbstractDataHandlerDescriptor;
import org.drombler.commons.data.BusinessObjectHandlerDescriptor;
import org.drombler.commons.data.file.DocumentHandlerDescriptor;
import org.osgi.framework.Bundle;
import org.softsmithy.lib.util.ResourceLoader;

/**
 *
 * @author puce
 */
public final class DataHandlerUtils {

    private DataHandlerUtils() {
    }

    public static DocumentHandlerDescriptor<?> createDocumentHandlerDescriptor(DocumentHandlerType documentHandler, Bundle bundle) throws ClassNotFoundException {
        Class<?> handlerClass = loadClass(bundle, documentHandler.getHandlerClass());
        return createDocumentHandlerDescriptor(documentHandler, handlerClass);
    }

    private static <D> DocumentHandlerDescriptor<D> createDocumentHandlerDescriptor(DocumentHandlerType documentHandler, Class<D> handlerClass) {
        DocumentHandlerDescriptor<D> documentHandlerDescriptor = new DocumentHandlerDescriptor<>();
        documentHandlerDescriptor.setMimeType(StringUtils.stripToNull(documentHandler.getMimeType()));
        configureDataHandlerDescriptor(documentHandlerDescriptor, documentHandler, handlerClass);
        return documentHandlerDescriptor;
    }

    private static <D> void configureDataHandlerDescriptor(AbstractDataHandlerDescriptor<D> descriptor, DataHandlerType dataHandlerType, Class<D> handlerClass) {
        descriptor.setIcon(StringUtils.stripToNull(dataHandlerType.getIcon()));
        descriptor.setResourceLoader(new ResourceLoader(handlerClass));
        descriptor.setDataHandlerClass(handlerClass);
    }

    public static BusinessObjectHandlerDescriptor<?> createBusinessObjectHandlerDescriptor(BusinessObjectHandlerType businessObjectHandler, Bundle bundle) throws ClassNotFoundException {
        Class<?> handlerClass = loadClass(bundle, businessObjectHandler.getHandlerClass());
        return createBusinessObjectHandlerDescriptor(businessObjectHandler, handlerClass);
    }

    private static <D> BusinessObjectHandlerDescriptor<D> createBusinessObjectHandlerDescriptor(BusinessObjectHandlerType businessObjectHandler,
            Class<D> handlerClass) {
        BusinessObjectHandlerDescriptor<D> descriptor = new BusinessObjectHandlerDescriptor<>();
        configureDataHandlerDescriptor(descriptor, businessObjectHandler, handlerClass);
        return descriptor;
    }

}
