/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.spi;

import org.apache.commons.lang.StringUtils;
import org.osgi.framework.Bundle;
import org.richclientplatform.core.docking.jaxb.EditorDockingType;

/**
 *
 * @author puce
 */
public class EditorDockingDescriptor extends AbstractDockableDockingDescriptor {

    private Class<?> dockableClass;

    /**
     * @return the dockableClass
     */
    public Class<?> getDockableClass() {
        return dockableClass;
    }

    /**
     * @param dockableClass the dockableClass to set
     */
    public void setDockableClass(Class<?> dockableClass) {
        this.dockableClass = dockableClass;
    }

    public static EditorDockingDescriptor createEditorDockingDescriptor(EditorDockingType docking, Bundle bundle) throws ClassNotFoundException {
        EditorDockingDescriptor dockingDescriptor = new EditorDockingDescriptor();
        dockingDescriptor.setId(StringUtils.stripToNull(docking.getId()));
        dockingDescriptor.setIcon(StringUtils.stripToNull(docking.getIcon()));
        dockingDescriptor.setAreaId(StringUtils.stripToNull(docking.getAreaId()));
        dockingDescriptor.setDockableClass(bundle.loadClass(StringUtils.stripToNull(docking.getDockableClass())));
        return dockingDescriptor;
    }
}
