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

import org.drombler.acp.core.docking.jaxb.EditorDockingType;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
public class EditorDockingDescriptor extends AbstractDockableDockingDescriptor {

    public static EditorDockingDescriptor createEditorDockingDescriptor(EditorDockingType docking, Bundle bundle) throws ClassNotFoundException {
        EditorDockingDescriptor dockingDescriptor = new EditorDockingDescriptor();

        DockingDescriptorUtils.configureDockingDescriptor(dockingDescriptor, docking, bundle);

        return dockingDescriptor;
    }
}
