package org.drombler.acp.core.data.spi;

import org.drombler.commons.data.file.FileExtensionDescriptor;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.commons.util.OSGiResourceBundleUtils;
import org.drombler.acp.core.data.jaxb.FileExtensionType;
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
