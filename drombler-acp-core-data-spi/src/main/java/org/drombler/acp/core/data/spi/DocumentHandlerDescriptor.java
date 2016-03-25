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
import org.softsmithy.lib.util.ResourceLoader;

/**
 *
 * @author puce
 */
public class DocumentHandlerDescriptor<D> {

    private String mimeType;
    private String icon;
    private Class<D> documentHandlerClass;
    private ResourceLoader resourceLoader;

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

    /**
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Class<D> getDocumentHandlerClass() {
        return documentHandlerClass;
    }

    /**
     * @param documentHandlerClass the documentHandlerClass to set
     */
    public void setDocumentHandlerClass(Class<D> documentHandlerClass) {
        this.documentHandlerClass = documentHandlerClass;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public static DocumentHandlerDescriptor<?> createDocumentTypeHandlerDescriptor(DocumentHandlerType documentHandler, Bundle bundle)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> handlerClass = bundle.loadClass(StringUtils.stripToNull(documentHandler.getHandlerClass()));
        return createDocumentTypeHandlerDescriptor(documentHandler, handlerClass);
    }

    private static <D> DocumentHandlerDescriptor<D> createDocumentTypeHandlerDescriptor(DocumentHandlerType documentHandler,
            Class<D> handlerClass) throws ClassNotFoundException {
        DocumentHandlerDescriptor<D> documentHandlerDescriptor = new DocumentHandlerDescriptor<>();
        documentHandlerDescriptor.setMimeType(StringUtils.stripToNull(documentHandler.getMimeType()));
        documentHandlerDescriptor.setIcon(StringUtils.stripToNull(documentHandler.getIcon()));
        documentHandlerDescriptor.setResourceLoader(new ResourceLoader(handlerClass));
        documentHandlerDescriptor.setDocumentHandlerClass(handlerClass);
        return documentHandlerDescriptor;
    }

    public D createDocumentHandler(Path filePath)
            throws IllegalAccessException, SecurityException, InvocationTargetException, InstantiationException, IllegalArgumentException, NoSuchMethodException {
        Constructor<? extends D> documentHandlerConstructor = getDocumentHandlerClass().getConstructor(Path.class);
        return documentHandlerConstructor.newInstance(filePath);
    }
}
