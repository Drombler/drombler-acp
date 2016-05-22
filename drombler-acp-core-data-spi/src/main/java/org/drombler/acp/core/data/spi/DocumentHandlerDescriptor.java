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
package org.drombler.acp.core.data.spi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.data.jaxb.DocumentHandlerType;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
public class DocumentHandlerDescriptor<D> extends AbstractDataHandlerDescriptor<D> {

    private String mimeType;

    /**
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public static DocumentHandlerDescriptor<?> createDocumentHandlerDescriptor(DocumentHandlerType documentHandler, Bundle bundle) throws ClassNotFoundException {
        Class<?> handlerClass = loadHandlerClass(documentHandler, bundle);
        return createDocumentHandlerDescriptor(documentHandler, handlerClass);
    }

    private static <D> DocumentHandlerDescriptor<D> createDocumentHandlerDescriptor(DocumentHandlerType documentHandler, Class<D> handlerClass) throws ClassNotFoundException {
        DocumentHandlerDescriptor<D> documentHandlerDescriptor = new DocumentHandlerDescriptor<>();
        documentHandlerDescriptor.setMimeType(StringUtils.stripToNull(documentHandler.getMimeType()));
        configureDataHandlerDescriptor(documentHandlerDescriptor, documentHandler, handlerClass);
        return documentHandlerDescriptor;
    }

    public D createDocumentHandler(Path filePath) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<? extends D> documentHandlerConstructor = getDataHandlerClass().getConstructor(Path.class);
        return documentHandlerConstructor.newInstance(filePath);
    }
}
