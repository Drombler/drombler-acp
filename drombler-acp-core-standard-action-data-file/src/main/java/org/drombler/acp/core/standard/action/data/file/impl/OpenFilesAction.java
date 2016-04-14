package org.drombler.acp.core.standard.action.data.file.impl;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.data.Openable;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptor;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistry;
import org.drombler.acp.core.data.spi.FileChooserProvider;
import org.drombler.acp.core.data.spi.FileExtensionDescriptor;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistry;
import org.drombler.commons.action.AbstractActionListener;
import org.drombler.commons.context.Context;
import org.drombler.commons.context.LocalContextProvider;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * @author puce
 */
// TODO: close AutoCloseable Actions
@Action(id = "open", category = "core", displayName = "%open.displayName", accelerator = "Shortcut+O")
@MenuEntry(path = "File", position = 1010)
public class OpenFilesAction extends AbstractActionListener<Object> implements AutoCloseable {

    private final ServiceTracker<FileChooserProvider, FileChooserProvider> fileChooserProviderServiceTracker;
    private final ServiceTracker<FileExtensionDescriptorRegistry, FileExtensionDescriptorRegistry> fileExtensionDescriptorRegistryServiceTracker;
    private final ServiceTracker<DocumentHandlerDescriptorRegistry, DocumentHandlerDescriptorRegistry> documentHandlerDescriptorRegistryServiceTracker;
    private FileChooserProvider fileChooserProvider;
    private FileExtensionDescriptorRegistry fileExtensionDescriptorRegistry;
    private DocumentHandlerDescriptorRegistry documentHandlerDescriptorRegistry;

    public OpenFilesAction() {
        this.fileChooserProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FileChooserProvider.class, this::setFileChooserProvider);
        this.fileExtensionDescriptorRegistryServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FileExtensionDescriptorRegistry.class, this::setFileExtensionDescriptorRegistry);
        this.documentHandlerDescriptorRegistryServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DocumentHandlerDescriptorRegistry.class, this::setDocumentHandlerDescriptorRegistry);
        this.fileChooserProviderServiceTracker.open(true);
        this.fileExtensionDescriptorRegistryServiceTracker.open(true);
        this.documentHandlerDescriptorRegistryServiceTracker.open(true);
        setEnabled(isInitialized());
    }

    @Override
    public void onAction(Object event) {
//        Path testFxmlPath = Paths.get("/fastdata/Programming/Java/drombler/drombler-scene-designer/tmp/test.fxml");
//        ContentPane contentPane = new ContentPane(testFxmlPath);
//        Dockables.inject(contentPane);
//        Dockables.open(contentPane);
        List<Path> filePaths = fileChooserProvider.showOpenMultipleDialog();
        if (filePaths != null) {
            filePaths.forEach(this::openFile);
        }
    }

    private void openFile(Path fileToOpen) {
        String extension = getExtension(fileToOpen);
        FileExtensionDescriptor fileExtensionDescriptor = getFileExtensionDescriptorRegistry().getFileExtensionDescriptor(extension);
        if (fileExtensionDescriptor != null) {
            String mimeType = fileExtensionDescriptor.getMimeType();
            DocumentHandlerDescriptor documentHandlerDescriptor = getDocumentHandlerDescriptorRegistry().getDocumentHandlerDescriptor(mimeType);
            if (documentHandlerDescriptor != null) {
                try {
                    Object documentHandler = documentHandlerDescriptor.createDocumentHandler(fileToOpen);
                    if (documentHandler instanceof LocalContextProvider) {
                        Context localContext = ((LocalContextProvider) documentHandler).getLocalContext();
                        Openable openable = localContext.find(Openable.class);
                        if (openable != null) {
                            openable.open(); // TODO: load them in background
                        } else {
// TODO
                        }
                    } else {

                    }// TODO
                } catch (IllegalAccessException | SecurityException | InvocationTargetException | InstantiationException | IllegalArgumentException | NoSuchMethodException ex) {
                    // TODO
                }
            } else {
// TODO
            }
        } else {
// TODO
        }
    }

    private boolean isInitialized() {
        return fileChooserProvider != null && fileExtensionDescriptorRegistry != null && documentHandlerDescriptorRegistry != null;
    }

    // TODO: use PathUtils from SoftSmithy
    public String getExtension(Path path) {
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

    /**
     * @return the fileChooserProvider
     */
    public FileChooserProvider getFileChooserProvider() {
        return fileChooserProvider;
    }

    /**
     * @param fileChooserProvider the fileChooserProvider to set
     */
    public void setFileChooserProvider(FileChooserProvider fileChooserProvider) {
        this.fileChooserProvider = fileChooserProvider;
        setEnabled(isInitialized());
    }

    /**
     * @return the fileExtensionDescriptorRegistry
     */
    public FileExtensionDescriptorRegistry getFileExtensionDescriptorRegistry() {
        return fileExtensionDescriptorRegistry;
    }

    /**
     * @param fileExtensionDescriptorRegistry the fileExtensionDescriptorRegistry to set
     */
    public void setFileExtensionDescriptorRegistry(FileExtensionDescriptorRegistry fileExtensionDescriptorRegistry) {
        this.fileExtensionDescriptorRegistry = fileExtensionDescriptorRegistry;
        setEnabled(isInitialized());
    }

    /**
     * @return the documentHandlerDescriptorRegistry
     */
    public DocumentHandlerDescriptorRegistry getDocumentHandlerDescriptorRegistry() {
        return documentHandlerDescriptorRegistry;
    }

    /**
     * @param documentHandlerDescriptorRegistry the documentHandlerDescriptorRegistry to set
     */
    public void setDocumentHandlerDescriptorRegistry(DocumentHandlerDescriptorRegistry documentHandlerDescriptorRegistry) {
        this.documentHandlerDescriptorRegistry = documentHandlerDescriptorRegistry;
        setEnabled(isInitialized());
    }

    @Override
    public void close() {
        this.fileChooserProviderServiceTracker.close();
        this.fileExtensionDescriptorRegistryServiceTracker.close();
        this.documentHandlerDescriptorRegistryServiceTracker.close();
    }

}
