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
import java.util.EnumMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.docking.jaxb.DockingAreaKindType;
import org.drombler.acp.core.docking.jaxb.DockingAreaType;
import org.drombler.commons.docking.DockingAreaDescriptor;
import org.drombler.commons.docking.DockingAreaKind;

/**
 *
 * @author puce
 */
public class DockingAreaDescriptorUtils {

    //TODO: not safe as changes to DockingAreaKind are missed at compile time. 
    // Possible solutions:
    // - Consider to generate a part of the XSD from DockingAreaKind and omit DockingAreaKindType
    // - Add a method toJaxb() to DockingAreaKind -> clutters the interface of the class (implementationg detail leaking in interface)
    // - Use a unit test to test if the static block throws an IllegalStateException (current solution) 
    //
    // Note: There is a similar issue when writing the XML in the annotation processor (changes to DockingAreaKind are missed at compile time)
    private static final Map<DockingAreaKindType, DockingAreaKind> DOCKING_AREA_KINDS = new EnumMap<>(
            DockingAreaKindType.class);

    static {
        DOCKING_AREA_KINDS.put(DockingAreaKindType.VIEW, DockingAreaKind.VIEW);
        DOCKING_AREA_KINDS.put(DockingAreaKindType.EDITOR, DockingAreaKind.EDITOR);
        for (DockingAreaKindType dockingAreaKind : DockingAreaKindType.values()) {
            if (!DOCKING_AREA_KINDS.containsKey(dockingAreaKind)) {
                throw new IllegalStateException("No mapping for: " + dockingAreaKind);
            }
        }
    }

    private DockingAreaDescriptorUtils() {
    }

    public static DockingAreaDescriptor createDockingAreaDescriptor(DockingAreaType dockingArea) {
        DockingAreaDescriptor dockingAreaDescriptor = new DockingAreaDescriptor();
        dockingAreaDescriptor.setId(StringUtils.stripToNull(dockingArea.getId()));
        dockingAreaDescriptor.setKind(DOCKING_AREA_KINDS.get(dockingArea.getKind()));
        dockingAreaDescriptor.setPosition(dockingArea.getPosition());
        dockingAreaDescriptor.setParentPath(new ArrayList<>(dockingArea.getPaths().getPath()));
        dockingAreaDescriptor.setPermanent(dockingArea.isPermanent());
        dockingAreaDescriptor.setLayoutConstraints(LayoutConstraintsDescriptorUtils.createLayoutConstraintsDescriptor(
                dockingArea.getLayoutConstraints()));
        return dockingAreaDescriptor;
    }
}
