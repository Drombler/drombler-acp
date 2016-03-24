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

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.commons.util.OSGiResourceBundleUtils;
import org.drombler.acp.core.data.jaxb.FileExtensionType;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
public class FileExtensionDescriptor {

    private String displayName;
    private String mimeType;
    private List<String> fileExtensions;

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
     * @return the fileExtensions
     */
    public List<String> getFileExtensions() {
        return fileExtensions;
    }

    /**
     * @param fileExtensions the fileExtensions to set
     */
    public void setFileExtensions(List<String> fileExtensions) {
        this.fileExtensions = fileExtensions;
    }

    public static FileExtensionDescriptor createFileExtensionDescriptor(FileExtensionType fileExtension, Bundle bundle) {
        FileExtensionDescriptor fileTypeHandlerDescriptor = new FileExtensionDescriptor();
        fileTypeHandlerDescriptor.setMimeType(StringUtils.stripToNull(fileExtension.getMimeType()));
        fileTypeHandlerDescriptor.setDisplayName(OSGiResourceBundleUtils.getPackageResourceStringPrefixed(fileExtension.getPackage(),
                fileExtension.getDisplayName(), bundle));
        fileTypeHandlerDescriptor.setFileExtensions(fileExtension.getFileExtension());
        return fileTypeHandlerDescriptor;
    }

}
