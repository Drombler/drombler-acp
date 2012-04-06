/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.richclientplatform.core.application.ExtensionPoint;
import org.richclientplatform.core.docking.jaxb.DockingAreasType;

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
