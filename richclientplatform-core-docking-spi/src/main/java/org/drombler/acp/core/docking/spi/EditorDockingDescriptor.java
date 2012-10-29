/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking.spi;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.drombler.acp.core.docking.jaxb.EditorDockingType;

/**
 *
 * @author puce
 */
public class EditorDockingDescriptor extends AbstractDockableDockingDescriptor {

    public static EditorDockingDescriptor createEditorDockingDescriptor(EditorDockingType docking, Bundle bundle) throws ClassNotFoundException {
        EditorDockingDescriptor dockingDescriptor = new EditorDockingDescriptor();
        dockingDescriptor.setId(StringUtils.stripToNull(docking.getId()));
        dockingDescriptor.setIcon(StringUtils.stripToNull(docking.getIcon()));
        dockingDescriptor.setAreaId(StringUtils.stripToNull(docking.getAreaId()));
        dockingDescriptor.setDockableClass(bundle.loadClass(StringUtils.stripToNull(docking.getDockableClass())));
        return dockingDescriptor;
    }
}
