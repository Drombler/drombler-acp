package org.drombler.acp.core.data.spi.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.data.spi.FileExtensionDescriptor;
import org.drombler.acp.core.data.spi.FileExtensionDescriptorRegistry;
import org.drombler.acp.core.data.spi.FileExtensionEvent;
import org.drombler.acp.core.data.spi.FileExtensionListener;

/**
 *
 * @author puce
 */
@Component
@Service
public class FileExtensionRegistryImpl implements FileExtensionDescriptorRegistry {

    private final Map<String, FileExtensionDescriptor> fileExtensions = new HashMap<>();
    private final Set<FileExtensionDescriptor> fileExtensionDescriptors = new HashSet<>();
    private final Set<FileExtensionListener> listeners = new HashSet<>();

    @Override
    public void registerFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor) {
        fileExtensionDescriptor.getFileExtensions().stream()
                .map(String::toLowerCase)
                .forEach(fileExtension -> fileExtensions.put(fileExtension, fileExtensionDescriptor));
        fileExtensionDescriptors.add(fileExtensionDescriptor);
        fireFileExtensionAdded(fileExtensionDescriptor);
    }

    @Override
    public void unregisterFileExtensionDescriptor(FileExtensionDescriptor fileExtensionDescriptor) {
        fileExtensionDescriptor.getFileExtensions().stream()
                .map(String::toLowerCase)
                .forEach(fileExtensions::remove);
        fileExtensionDescriptors.remove(fileExtensionDescriptor);
        fireFileExtensionRemoved(fileExtensionDescriptor);
    }

    @Override
    public FileExtensionDescriptor getFileExtensionDescriptor(String fileExtension) {
        return fileExtensions.get(fileExtension.toLowerCase());
    }

    @Override
    public void registerFileExtensionListener(FileExtensionListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterFileExtensionListener(FileExtensionListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Set<FileExtensionDescriptor> getAllFileExtensionDescriptors() {
        return Collections.unmodifiableSet(fileExtensionDescriptors);
    }

    private void fireFileExtensionAdded(FileExtensionDescriptor fileExtensionDescriptor) {
        FileExtensionEvent event = new FileExtensionEvent(this, fileExtensionDescriptor);
        listeners.forEach(listener -> listener.fileExtensionAdded(event));
    }

    private void fireFileExtensionRemoved(FileExtensionDescriptor fileExtensionDescriptor) {
        FileExtensionEvent event = new FileExtensionEvent(this, fileExtensionDescriptor);
        listeners.forEach(listener -> listener.fileExtensionRemoved(event));
    }
}
