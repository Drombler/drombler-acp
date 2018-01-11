package org.drombler.acp.core.standard.action.data.file.impl;

import java.nio.file.Path;
import java.util.List;
import org.drombler.acp.core.action.Action;
import org.drombler.acp.core.action.MenuEntry;
import org.drombler.acp.core.commons.util.SimpleServiceTrackerCustomizer;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadConsumer;
import org.drombler.acp.core.commons.util.concurrent.ApplicationThreadExecutorProvider;
import org.drombler.acp.core.context.ContextManagerProvider;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.drombler.acp.core.data.spi.DocumentHandlerDescriptorRegistryProvider;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistryProvider;
import org.drombler.commons.action.AbstractActionListener;
import org.drombler.commons.client.dialog.FileChooserProvider;
import org.drombler.commons.context.ContextInjector;
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

    private final ServiceTracker<ApplicationThreadExecutorProvider, ApplicationThreadExecutorProvider> applicationThreadExecutorProviderServiceTracker;
    private ServiceTracker<FileChooserProvider, FileChooserProvider> fileChooserProviderServiceTracker;
    private ServiceTracker<FileExtensionDescriptorRegistryProvider, FileExtensionDescriptorRegistryProvider> fileExtensionDescriptorRegistryProviderServiceTracker;
    private ServiceTracker<DocumentHandlerDescriptorRegistryProvider, DocumentHandlerDescriptorRegistryProvider> documentHandlerDescriptorRegistryServiceTracker;
    private ServiceTracker<DataHandlerRegistryProvider, DataHandlerRegistryProvider> dataHandlerRegistryServiceTracker;
    private ServiceTracker<ContextManagerProvider, ContextManagerProvider> contextManagerServiceTracker;
    private FileChooserProvider fileChooserProvider;
    private FileExtensionDescriptorRegistryProvider fileExtensionDescriptorRegistryProvider;
    private DocumentHandlerDescriptorRegistryProvider documentHandlerDescriptorRegistryProvider;
    private DataHandlerRegistryProvider dataHandlerRegistryProvider;
    private ApplicationThreadExecutorProvider applicationThreadExecutorProvider;
    private ContextManagerProvider contextManagerProvider;
    private ContextInjector contextInjector;

    public OpenFilesAction() {
        this.applicationThreadExecutorProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FrameworkUtil.getBundle(OpenFilesAction.class).getBundleContext(),
                ApplicationThreadExecutorProvider.class, this::setApplicationThreadExecutorProvider);
        this.applicationThreadExecutorProviderServiceTracker.open(true);
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
        FileUtils.openFile(fileToOpen, dataHandlerRegistryProvider.getDataHandlerRegistry(), fileExtensionDescriptorRegistryProvider.getFileExtensionDescriptorRegistry(),
                documentHandlerDescriptorRegistryProvider.getDocumentHandlerDescriptorRegistry(), contextManagerProvider.getContextManager(), contextInjector);
    }

    /**
     * @return the applicationExecutorProvider
     */
    public ApplicationThreadExecutorProvider getApplicationThreadExecutorProvider() {
        return applicationThreadExecutorProvider;
    }

    /**
     * @param applicationThreadExecutorProvider the applicationExecutorProvider to set
     */
    public void setApplicationThreadExecutorProvider(ApplicationThreadExecutorProvider applicationThreadExecutorProvider) {
        this.applicationThreadExecutorProvider = applicationThreadExecutorProvider;
        if (this.applicationThreadExecutorProvider != null) {
            this.fileChooserProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FileChooserProvider.class,
                    new ApplicationThreadConsumer<>(this.applicationThreadExecutorProvider, this::setFileChooserProvider));
            this.fileExtensionDescriptorRegistryProviderServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(FileExtensionDescriptorRegistryProvider.class,
                    new ApplicationThreadConsumer<>(this.applicationThreadExecutorProvider, this::setFileExtensionDescriptorRegistryProvider));
            this.documentHandlerDescriptorRegistryServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DocumentHandlerDescriptorRegistryProvider.class,
                    new ApplicationThreadConsumer<>(this.applicationThreadExecutorProvider, this::setDocumentHandlerDescriptorRegistryProvider));
            this.dataHandlerRegistryServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(DataHandlerRegistryProvider.class,
                    new ApplicationThreadConsumer<>(this.applicationThreadExecutorProvider, this::setDataHandlerRegistryProvider));
            this.contextManagerServiceTracker = SimpleServiceTrackerCustomizer.createServiceTracker(ContextManagerProvider.class,
                    new ApplicationThreadConsumer<>(this.applicationThreadExecutorProvider, this::setContextManagerProvider));
            this.fileChooserProviderServiceTracker.open(true);
            this.fileExtensionDescriptorRegistryProviderServiceTracker.open(true);
            this.documentHandlerDescriptorRegistryServiceTracker.open(true);
            this.dataHandlerRegistryServiceTracker.open(true);
            this.contextManagerServiceTracker.open(true);
        } else {
            this.fileChooserProviderServiceTracker.close();
            this.fileChooserProviderServiceTracker = null;
            this.fileExtensionDescriptorRegistryProviderServiceTracker.close();
            this.fileExtensionDescriptorRegistryProviderServiceTracker = null;
            this.documentHandlerDescriptorRegistryServiceTracker.close();
            this.documentHandlerDescriptorRegistryServiceTracker = null;
            this.dataHandlerRegistryServiceTracker.close();
            this.dataHandlerRegistryServiceTracker = null;
            this.contextManagerServiceTracker.close();
            this.contextManagerServiceTracker = null;
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

    public void setContextManagerProvider(ContextManagerProvider contextManagerProvider) {
        this.contextManagerProvider = contextManagerProvider;
        if (contextManagerProvider != null) {
            this.contextInjector = new ContextInjector(contextManagerProvider.getContextManager());
        } else {
            this.contextInjector = null;
        }
        setEnabled(isInitialized());
    }

    @Override
    public void close() {
        this.applicationThreadExecutorProviderServiceTracker.close();
    }

}
