package org.drombler.acp.core.docking.spi.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.docking.spi.EditorDockingDescriptor;
import org.drombler.acp.core.docking.spi.EditorDockingDescriptorRegistry;

/**
 *
 * @author puce
 */
@Component
@Service
public class EditorDockingDescriptorRegistryImpl<D> implements EditorDockingDescriptorRegistry<D> {

    private final Map<Class<?>, EditorDockingDescriptor<? extends D>> editors = new HashMap<>();
    private final Map<Class<? extends D>, Class<?>> contentTypes = new HashMap<>();

    @Override
    public void registerEditorDockingDescriptor(Class<?> contentType, EditorDockingDescriptor<? extends D> editorDockingDescriptor) {
        editors.put(contentType, editorDockingDescriptor);
        contentTypes.put(editorDockingDescriptor.getDockableClass(), contentType);
    }

    @Override
    public EditorDockingDescriptor<? extends D> getEditorDockingDescriptor(Class<?> contentType) {
        return editors.get(contentType);
    }

    @Override
    public Class<?> getContentType(Class<? extends D> editorType) {
        return contentTypes.get(editorType);
    }

}
