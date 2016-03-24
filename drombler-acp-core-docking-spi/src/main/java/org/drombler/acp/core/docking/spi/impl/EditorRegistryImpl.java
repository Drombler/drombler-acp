package org.drombler.acp.core.docking.spi.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.docking.spi.EditorRegistry;

/**
 *
 * @author puce
 */
@Component
@Service
public class EditorRegistryImpl<D> implements EditorRegistry<D> {

    private final Map<Class<?>, Class<? extends D>> editors = new HashMap<>();

    @Override
    public void registerEditorClass(Class<?> contentType, Class<? extends D> editorClass) {
        editors.put(contentType, editorClass);
    }

    @Override
    public Class<? extends D> getEditorClass(Class<?> contentType) {
        return editors.get(contentType);
    }

}
