/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2012 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
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
