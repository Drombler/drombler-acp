package org.drombler.acp.core.docking.spi.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.data.DataCapabilityProvider;
import org.drombler.acp.core.data.Openable;
import org.drombler.acp.core.docking.spi.Dockables;

@Component
@Service
public class EditorOpener implements DataCapabilityProvider<Openable> {

    @Override
    public Openable getDataCapability(Object content) {
        return () -> Dockables.openEditorForContent(content);
    }

}
