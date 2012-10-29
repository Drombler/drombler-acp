/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.application.ExtensionPoint;
import org.drombler.acp.core.docking.jaxb.DockingAreasType;

/**
 *
 * @author puce
 */
@Component
@Service
public class DockingAreasExtensionPoint implements ExtensionPoint<DockingAreasType> {

    @Override
    public Class<DockingAreasType> getJAXBRootClass() {
        return DockingAreasType.class;
    }


}
