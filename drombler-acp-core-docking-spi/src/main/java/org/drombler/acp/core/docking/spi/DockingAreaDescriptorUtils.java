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

import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.docking.jaxb.DockingAreaType;
import org.drombler.commons.client.docking.DockingAreaDescriptor;

/**
 *
 * @author puce
 */
public class DockingAreaDescriptorUtils {

    public static DockingAreaDescriptor createDockingAreaDescriptor(DockingAreaType dockingArea) {
        DockingAreaDescriptor dockingAreaDescriptor = new DockingAreaDescriptor();
        dockingAreaDescriptor.setId(StringUtils.stripToNull(dockingArea.getId()));
        dockingAreaDescriptor.setPosition(dockingArea.getPosition());
        dockingAreaDescriptor.setPath(new ArrayList<>(dockingArea.getPaths().getPath()));
        dockingAreaDescriptor.setPermanent(dockingArea.isPermanent());
        dockingAreaDescriptor.setLayoutConstraints(LayoutConstraintsDescriptorUtils.createLayoutConstraintsDescriptor(
                dockingArea.getLayoutConstraints()));
        return dockingAreaDescriptor;
    }
}
