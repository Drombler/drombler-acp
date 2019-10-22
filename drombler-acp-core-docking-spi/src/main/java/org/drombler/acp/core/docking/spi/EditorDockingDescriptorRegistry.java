package org.drombler.acp.core.docking.spi;

/**
 * A registry for {@link EditorDockingDescriptor}s.<br>
 * <br>
 * Note: this module also provides an implementation registered as an OSGi service.
 *
 * @author puce
 * @param <D> the base type of the editors
 */
public interface EditorDockingDescriptorRegistry<D> {

    /**
     * Registers an {@link EditorDockingDescriptor} for a content type.
     *
     * @param contentType the content type of the editor
     * @param editorDockingDescriptor the editor docking descriptor for the editor of the content type
     */
    void registerEditorDockingDescriptor(Class<?> contentType, EditorDockingDescriptor<? extends D> editorDockingDescriptor);

    /**
     * Unregisters the {@link EditorDockingDescriptor}, which has been registered for the specified content type.
     *
     * @param contentType the content type of the editor docking descriptor
     * @return the previously registered editor docking descriptor
     */
    EditorDockingDescriptor<? extends D> unregisterEditorDockingDescriptor(Class<?> contentType);

    /**
     * Gets the {@link EditorDockingDescriptor} for the provided content type.
     *
     * @param contentType the content type
     * @return the editor docking descriptor for the provided content type
     */
    EditorDockingDescriptor<? extends D> getEditorDockingDescriptor(Class<?> contentType);

    /**
     * Gets the content type for the provided editor type.
     *
     * @param editorType the editor type
     * @return the content type for the provided editor type
     * @see EditorDockingDescriptor#getDockableClass()
     * @see EditorDockingDescriptor#getContentType()
     */
    Class<?> getContentType(Class<? extends D> editorType);
}
