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

import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.data.jaxb.DocumentHandlerType;
import org.drombler.commons.client.util.ResourceBundleUtils;
import org.osgi.framework.Bundle;
import org.softsmithy.lib.util.ResourceLoader;

/**
 *
 * @author puce
 */
public class DocumentHandlerDescriptor {

    private String mimeType;
    private String displayName;
    private String icon;
    private Object documentHandler;
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
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public Object getDocumentHandler() {
        return documentHandler;
    }

    /**
     * @param documentHandler the documentHandler to set
     */
    public void setDocumentHandler(Object documentHandler) {
        this.documentHandler = documentHandler;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public static DocumentHandlerDescriptor createFileTypeHandlerDescriptor(DocumentHandlerType documentHandlerType,
            Bundle bundle)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> handlerClass = bundle.loadClass(StringUtils.stripToNull(documentHandlerType.getHandlerClass()));
        DocumentHandlerDescriptor fileTypeHandlerDescriptor = new DocumentHandlerDescriptor();
        fileTypeHandlerDescriptor.setMimeType(StringUtils.stripToNull(documentHandlerType.getMimeType()));
        fileTypeHandlerDescriptor.setDisplayName(ResourceBundleUtils.getPackageResourceStringPrefixed(handlerClass,
                documentHandlerType.getDisplayName()));
        fileTypeHandlerDescriptor.setIcon(StringUtils.stripToNull(documentHandlerType.getIcon()));
        fileTypeHandlerDescriptor.setResourceLoader(new ResourceLoader(handlerClass));
        Object handler = handlerClass.newInstance();
//        contextInjector.inject(documentHandler);
        fileTypeHandlerDescriptor.setDocumentHandler(handler);
        return fileTypeHandlerDescriptor;
    }
}
