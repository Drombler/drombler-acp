package org.drombler.acp.core.docking.spi;

/**
 *
 * @author puce
 */
public interface EditorDockingDescriptorRegistry<D> {

    void registerEditorDockingDescriptor(Class<?> contentType, EditorDockingDescriptor<? extends D> editorDockingDescriptor);

    EditorDockingDescriptor<? extends D> getEditorDockingDescriptor(Class<?> contentType);
}
