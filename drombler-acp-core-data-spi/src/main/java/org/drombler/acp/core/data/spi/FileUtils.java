package org.drombler.acp.core.data.spi;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.drombler.acp.core.data.Openable;
import org.drombler.commons.context.Contexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
public class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    public static void openFile(Path fileToOpen, FileExtensionDescriptorRegistry fileExtensionDescriptorRegistry, DocumentHandlerDescriptorRegistry documentHandlerDescriptorRegistry) {
        LOG.debug("Start opening file {}...", fileToOpen);
        String extension = getExtension(fileToOpen);
        FileExtensionDescriptor fileExtensionDescriptor = fileExtensionDescriptorRegistry.getFileExtensionDescriptor(extension);
        if (fileExtensionDescriptor != null) {
            String mimeType = fileExtensionDescriptor.getMimeType();
            DocumentHandlerDescriptor documentHandlerDescriptor = documentHandlerDescriptorRegistry.getDocumentHandlerDescriptor(mimeType);
            if (documentHandlerDescriptor != null) {
                try {
                    Object documentHandler = documentHandlerDescriptor.createDocumentHandler(fileToOpen);
                    Openable openable = Contexts.find(documentHandler, Openable.class);
                    if (openable != null) {
                        openable.open(); // TODO: load them in background
                    } else {
                        LOG.warn("No Openable found for " + documentHandler + "! "
                                + "The document handler either does not implement LocalContextProvider or does not observe registered DataCapabilityProvider.");
                    }
                } catch (IllegalAccessException | SecurityException | InvocationTargetException | InstantiationException | IllegalArgumentException | NoSuchMethodException ex) {
                    LOG.error("Could not create a document handler for " + fileToOpen + "!", ex);
                }
            } else {
                LOG.warn("No DocumentHandlerDescriptor found for:" + mimeType + "!");
            }
        } else {
            LOG.warn("No FileExtensionDescriptor found for:" + extension + "!");
        }
    }

    // TODO: use PathUtils from SoftSmithy
    private static String getExtension(Path path) {
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("Not a regular file: " + path.toString());
        }
        String extension = "";
        String fileName = path.getFileName().toString();
        int index = fileName.lastIndexOf('.');

        if (index > 0 && index < fileName.length() - 1) {
            extension = fileName.substring(index + 1).toLowerCase();
        }
        return "." + extension;
    }
}
