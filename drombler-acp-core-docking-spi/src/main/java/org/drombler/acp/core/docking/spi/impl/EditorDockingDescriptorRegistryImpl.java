package org.drombler.acp.core.docking.spi.impl;

import java.util.HashMap;
import java.util.Map;
import org.drombler.acp.core.docking.spi.EditorDockingDescriptor;
import org.drombler.acp.core.docking.spi.EditorDockingDescriptorRegistry;
import org.osgi.service.component.annotations.Component;

/**
 *
 * @author puce
 */
@Component
public class EditorDockingDescriptorRegistryImpl<D> implements EditorDockingDescriptorRegistry<D> {

    private final Map<Class<?>, EditorDockingDescriptor<? extends D>> editors = new HashMap<>();
    private final Map<Class<? extends D>, Class<?>> contentTypes = new HashMap<>();

    @Override
    public void registerEditorDockingDescriptor(Class<?> contentType, EditorDockingDescriptor<? extends D> editorDockingDescriptor) {
        editors.put(contentType, editorDockingDescriptor);
        contentTypes.put(editorDockingDescriptor.getDockableClass(), contentType);
    }

    @Override
    public EditorDockingDescriptor<? extends D> unregisterEditorDockingDescriptor(Class<?> contentType) {
        EditorDockingDescriptor<? extends D> editorDockingDescriptor = editors.remove(contentType);
        contentTypes.remove(editorDockingDescriptor.getDockableClass());
        return editorDockingDescriptor;
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
