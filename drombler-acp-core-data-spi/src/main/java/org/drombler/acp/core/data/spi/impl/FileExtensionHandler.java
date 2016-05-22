package org.drombler.acp.core.data.spi.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.drombler.acp.core.data.jaxb.FileExtensionType;
import org.drombler.acp.core.data.jaxb.FileExtensionsType;
import org.drombler.acp.core.data.spi.FileExtensionDescriptor;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@References({
    @Reference(name = "fileExtensionsType", referenceInterface = FileExtensionsType.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC),
    @Reference(name = "fileExtensionDescriptor", referenceInterface = FileExtensionDescriptor.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
})
public class FileExtensionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FileExtensionHandler.class);

    private final List<FileExtensionDescriptor> unresolvedFileExtensionDescriptors = new ArrayList<>();

    @Reference
    private FileExtensionDescriptorRegistry fileExtensionRegistry;

    protected void bindFileExtensionsType(ServiceReference<FileExtensionsType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        FileExtensionsType fileExtensionsType = context.getService(serviceReference);
        registerFileExtensions(fileExtensionsType, context);
    }

    protected void unbindFileExtensionsType(FileExtensionsType fileExtensionsType) {
        // TODO
    }

    protected void bindFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor) {
        registerFileExtensionDescriptor(fileExtensionDescriptor);
    }

    protected void unbindFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor) {
        // TODO
    }

    protected void bindFileExtensionRegistry(FileExtensionDescriptorRegistry fileExtensionRegistry) {
        this.fileExtensionRegistry = fileExtensionRegistry;
    }

    protected void unbindFileExtensionRegistry(FileExtensionDescriptorRegistry fileExtensionRegistry) {
        // TODO
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedFileExtensionDescriptors();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    private void registerFileExtensions(FileExtensionsType fileExtensionsType, BundleContext context) {
        fileExtensionsType.getFileExtension().forEach(fileExtensionType
                -> registerFileExtension(fileExtensionType, context));
    }

    private void registerFileExtension(FileExtensionType fileExtensionType, BundleContext context) {
        // TODO: register FileExtensionDescriptor as service?
        FileExtensionDescriptor fileExtensionDescriptor = FileExtensionDescriptor.createFileExtensionDescriptor(
                fileExtensionType, context.getBundle());
        registerFileExtensionDescriptor(fileExtensionDescriptor);
    }

    private void registerFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor) {
        if (isInitialized()) {
            fileExtensionRegistry.registerFileExtensionDescriptor(fileExtensionDescriptor);
        } else {
            unresolvedFileExtensionDescriptors.add(fileExtensionDescriptor);
        }
    }

    private void resolveUnresolvedFileExtensionDescriptors() {
        unresolvedFileExtensionDescriptors.forEach(this::registerFileExtensionDescriptor);
    }

    private boolean isInitialized() {
        return fileExtensionRegistry != null;
    }
}
