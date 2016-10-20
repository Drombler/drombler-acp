package org.drombler.acp.core.docking.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.docking.spi.Dockables;
import org.drombler.commons.data.DataCapabilityProvider;
import org.drombler.commons.data.DataHandler;
import org.drombler.commons.data.Openable;

@Component
@Service
public class EditorOpener implements DataCapabilityProvider<Openable> {

    @Override
    public Openable getDataCapability(DataHandler<?> content) {
        return () -> Dockables.openEditorForContent(content);
    }

}
