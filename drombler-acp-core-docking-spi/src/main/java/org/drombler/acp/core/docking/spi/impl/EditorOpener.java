package org.drombler.acp.core.docking.spi.impl;

import org.drombler.acp.core.docking.spi.Dockables;
import org.drombler.commons.data.DataCapabilityProvider;
import org.drombler.commons.data.DataHandler;
import org.drombler.commons.data.Openable;
import org.osgi.service.component.annotations.Component;

@Component
public class EditorOpener implements DataCapabilityProvider<Openable> {

    @Override
    public Openable getDataCapability(DataHandler<?> content) {
        return () -> Dockables.openEditorForContent(content);
    }

}
