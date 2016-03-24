package org.drombler.acp.core.docking.spi;

/**
 *
 * @author puce
 */
public interface EditorRegistry<D> {

    void registerEditorClass(Class<?> contentType, Class<? extends D> editorClass);

    Class<? extends D> getEditorClass(Class<?> contentType);
}
