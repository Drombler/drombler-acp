package org.drombler.acp.core.standard.action.data.file.impl;

import java.nio.file.Path;
import java.util.List;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistryProvider;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistryProvider;
import org.drombler.commons.action.AbstractActionListener;
import org.drombler.commons.client.dialog.FileChooserProvider;
import org.drombler.commons.data.file.FileUtils;
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
    private final ServiceTracker<FileExtensionDescriptorRegistryProvider, FileExtensionDescriptorRegistryProvider> fileExtensionDescriptorRegistryProviderServiceTracker;
    private final ServiceTracker<DocumentHandlerDescriptorRegistryProvider, DocumentHandlerDescriptorRegistryProvider> documentHandlerDescriptorRegistryServiceTracker;
    private FileChooserProvider fileChooserProvider;
    private FileExtensionDescriptorRegistryProvider fileExtensionDescriptorRegistryProvider;
    private DocumentHandlerDescriptorRegistryProvider documentHandlerDescriptorRegistryProvider;

    public OpenFilesAction() {
        this.fileChooserProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FileChooserProvider.class, this::setFileChooserProvider);
        this.fileExtensionDescriptorRegistryProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FileExtensionDescriptorRegistryProvider.class,
                this::setFileExtensionDescriptorRegistryProvider);
        this.documentHandlerDescriptorRegistryServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DocumentHandlerDescriptorRegistryProvider.class,
                this::setDocumentHandlerDescriptorRegistryProvider);
        this.fileChooserProviderServiceTracker.open(true);
        this.fileExtensionDescriptorRegistryProviderServiceTracker.open(true);
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

    private boolean isInitialized() {
        return fileChooserProvider != null && fileExtensionDescriptorRegistryProvider != null && documentHandlerDescriptorRegistryProvider != null;
    }

    private void openFile(Path fileToOpen) {
        FileUtils.openFile(fileToOpen, fileExtensionDescriptorRegistryProvider.getFileExtensionDescriptorRegistry(), documentHandlerDescriptorRegistryProvider.getDocumentHandlerDescriptorRegistry());
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
    public FileExtensionDescriptorRegistryProvider getFileExtensionDescriptorRegistryProvider() {
        return fileExtensionDescriptorRegistryProvider;
    }

    /**
     * @param fileExtensionDescriptorRegistry the fileExtensionDescriptorRegistry to set
     */
    public void setFileExtensionDescriptorRegistryProvider(FileExtensionDescriptorRegistryProvider fileExtensionDescriptorRegistryProvider) {
        this.fileExtensionDescriptorRegistryProvider = fileExtensionDescriptorRegistryProvider;
        setEnabled(isInitialized());
    }

    /**
     * @return the documentHandlerDescriptorRegistry
     */
    public DocumentHandlerDescriptorRegistryProvider getDocumentHandlerDescriptorRegistryProvider() {
        return documentHandlerDescriptorRegistryProvider;
    }

    /**
     * @param documentHandlerDescriptorRegistry the documentHandlerDescriptorRegistry to set
     */
    public void setDocumentHandlerDescriptorRegistryProvider(DocumentHandlerDescriptorRegistryProvider documentHandlerDescriptorRegistryProvider) {
        this.documentHandlerDescriptorRegistryProvider = documentHandlerDescriptorRegistryProvider;
        setEnabled(isInitialized());
    }

    @Override
    public void close() {
        this.fileChooserProviderServiceTracker.close();
        this.fileExtensionDescriptorRegistryProviderServiceTracker.close();
        this.documentHandlerDescriptorRegistryServiceTracker.close();
    }

}
