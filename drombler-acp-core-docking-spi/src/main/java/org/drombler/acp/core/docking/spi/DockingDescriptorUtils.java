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

import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.docking.jaxb.AbstractDockingType;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
public class DockingDescriptorUtils {

    public static void configureDockingDescriptor(AbstractDockableDockingDescriptor dockingDescriptor,
            AbstractDockingType dockingType, Bundle bundle) throws ClassNotFoundException {
        dockingDescriptor.setId(StringUtils.stripToNull(dockingType.getId()));
        dockingDescriptor.setIcon(StringUtils.stripToNull(dockingType.getIcon()));
        dockingDescriptor.setAreaId(StringUtils.stripToNull(dockingType.getAreaId()));
        dockingDescriptor.setDockableClass(bundle.loadClass(StringUtils.stripToNull(dockingType.getDockableClass())));
    }
}
