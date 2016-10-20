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

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.commons.util.OSGiResourceBundleUtils;
import org.drombler.acp.core.data.jaxb.FileExtensionType;
import org.drombler.commons.data.file.FileExtensionDescriptor;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
public final class FileExtensionUtils {

    private FileExtensionUtils() {
    }

    public static FileExtensionDescriptor createFileExtensionDescriptor(FileExtensionType fileExtension, Bundle bundle) {
        String displayName = OSGiResourceBundleUtils.getPackageResourceStringPrefixed(fileExtension.getPackage(),
                fileExtension.getDisplayName(), bundle);
        String mimeType = StringUtils.stripToNull(fileExtension.getMimeType());
        List<String> fileExtensions = fileExtension.getFileExtension();
        return new FileExtensionDescriptor(displayName, mimeType, fileExtensions);
    }
}
