package org.drombler.acp.core.standard.action.data.file.impl;

import java.nio.file.Path;
import java.util.List;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadConsumer;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistryProvider;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistryProvider;
import org.drombler.acp.startup.main.ApplicationExecutorProvider;
import org.drombler.commons.action.AbstractActionListener;
import org.drombler.commons.client.dialog.FileChooserProvider;
import org.drombler.commons.data.file.FileUtils;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * @author puce
 */
// TODO: close AutoCloseable Actions
@Action(id = "open", category = "core", displayName = "%open.displayName", accelerator = "Shortcut+O")
@MenuEntry(path = "File", position = 1010)
public class OpenFilesAction extends AbstractActionListener<Object> implements AutoCloseable {

    private final ServiceTracker<ApplicationExecutorProvider, ApplicationExecutorProvider> applicationExecutorProviderServiceTracker;
    private ServiceTracker<FileChooserProvider, FileChooserProvider> fileChooserProviderServiceTracker;
    private ServiceTracker<FileExtensionDescriptorRegistryProvider, FileExtensionDescriptorRegistryProvider> fileExtensionDescriptorRegistryProviderServiceTracker;
    private ServiceTracker<DocumentHandlerDescriptorRegistryProvider, DocumentHandlerDescriptorRegistryProvider> documentHandlerDescriptorRegistryServiceTracker;
    private ServiceTracker<DataHandlerRegistryProvider, DataHandlerRegistryProvider> dataHandlerRegistryServiceTracker;
    private FileChooserProvider fileChooserProvider;
    private FileExtensionDescriptorRegistryProvider fileExtensionDescriptorRegistryProvider;
    private DocumentHandlerDescriptorRegistryProvider documentHandlerDescriptorRegistryProvider;
    private DataHandlerRegistryProvider dataHandlerRegistryProvider;
    private ApplicationExecutorProvider applicationExecutorProvider;

    public OpenFilesAction() {
        this.applicationExecutorProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FrameworkUtil.getBundle(OpenFilesAction.class).getBundleContext(),
                ApplicationExecutorProvider.class, this::setApplicationExecutorProvider);
        this.applicationExecutorProviderServiceTracker.open(true);
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
        return fileChooserProvider != null && fileExtensionDescriptorRegistryProvider != null && documentHandlerDescriptorRegistryProvider != null
                && dataHandlerRegistryProvider != null;
    }

    private void openFile(Path fileToOpen) {
        FileUtils.openFile(fileToOpen, dataHandlerRegistryProvider.getDataHandlerRegistry(), fileExtensionDescriptorRegistryProvider.getFileExtensionDescriptorRegistry(), documentHandlerDescriptorRegistryProvider.getDocumentHandlerDescriptorRegistry());
    }

    /**
     * @return the applicationExecutorProvider
     */
    public ApplicationExecutorProvider getApplicationExecutorProvider() {
        return applicationExecutorProvider;
    }

    /**
     * @param applicationExecutorProvider the applicationExecutorProvider to set
     */
    public void setApplicationExecutorProvider(ApplicationExecutorProvider applicationExecutorProvider) {
        this.applicationExecutorProvider = applicationExecutorProvider;
        if (this.applicationExecutorProvider != null) {
            this.fileChooserProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FileChooserProvider.class,
                    new ApplicationThreadConsumer<>(this.applicationExecutorProvider, this::setFileChooserProvider));
            this.fileExtensionDescriptorRegistryProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FileExtensionDescriptorRegistryProvider.class,
                    new ApplicationThreadConsumer<>(this.applicationExecutorProvider, this::setFileExtensionDescriptorRegistryProvider));
            this.documentHandlerDescriptorRegistryServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DocumentHandlerDescriptorRegistryProvider.class,
                    new ApplicationThreadConsumer<>(this.applicationExecutorProvider, this::setDocumentHandlerDescriptorRegistryProvider));
            this.dataHandlerRegistryServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DataHandlerRegistryProvider.class,
                    new ApplicationThreadConsumer<>(this.applicationExecutorProvider, this::setDataHandlerRegistryProvider));
            this.fileChooserProviderServiceTracker.open(true);
            this.fileExtensionDescriptorRegistryProviderServiceTracker.open(true);
            this.documentHandlerDescriptorRegistryServiceTracker.open(true);
            this.dataHandlerRegistryServiceTracker.open(true);
        } else {
            this.fileChooserProviderServiceTracker.close();
            this.fileChooserProviderServiceTracker = null;
            this.fileExtensionDescriptorRegistryProviderServiceTracker.close();
            this.fileExtensionDescriptorRegistryProviderServiceTracker = null;
            this.documentHandlerDescriptorRegistryServiceTracker.close();
            this.documentHandlerDescriptorRegistryServiceTracker = null;
            this.dataHandlerRegistryServiceTracker.close();
            this.dataHandlerRegistryServiceTracker = null;
        }
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

    /**
     * @return the dataHandlerRegistryProvider
     */
    public DataHandlerRegistryProvider getDataHandlerRegistryProvider() {
        return dataHandlerRegistryProvider;
    }

    /**
     * @param dataHandlerRegistryProvider the dataHandlerRegistryProvider to set
     */
    public void setDataHandlerRegistryProvider(DataHandlerRegistryProvider dataHandlerRegistryProvider) {
        this.dataHandlerRegistryProvider = dataHandlerRegistryProvider;
        setEnabled(isInitialized());
    }

    @Override
    public void close() {
        this.applicationExecutorProviderServiceTracker.close();
    }

}
