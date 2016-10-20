package org.drombler.acp.core.docking.spi;

/**
 *
 * @author puce
 * @param <D> the base type of the editors
 */
public interface EditorDockingDescriptorRegistry<D> {

    void registerEditorDockingDescriptor(Class<?> contentType, EditorDockingDescriptor<? extends D> editorDockingDescriptor);

    EditorDockingDescriptor<? extends D> getEditorDockingDescriptor(Class<?> contentType);

    Class<?> getContentType(Class<? extends D> editorType);
}
