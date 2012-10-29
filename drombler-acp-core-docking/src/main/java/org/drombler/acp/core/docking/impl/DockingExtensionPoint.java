/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.application.ExtensionPoint;
import org.drombler.acp.core.docking.jaxb.DockingsType;

/**
 *
 * @author puce
 */
@Component
@Service
public class DockingExtensionPoint implements ExtensionPoint<DockingsType> {

    @Override
    public Class<DockingsType> getJAXBRootClass() {
        return DockingsType.class;
    }


}
