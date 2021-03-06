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

import org.drombler.acp.core.docking.jaxb.LayoutConstraintsType;
import org.drombler.commons.docking.LayoutConstraintsDescriptor;

/**
 * A utility class for {@link LayoutConstraintsDescriptor}s.
 *
 * @author puce
 */
public final class LayoutConstraintsDescriptorUtils {

    private LayoutConstraintsDescriptorUtils() {
    }

    /**
     * Creates an instance of a {@link LayoutConstraintsDescriptor} from a {@link LayoutConstraintsType} unmarshalled from the application.xml.
     *
     * @param layoutConstraints the unmarshalled LayoutConstraintsType
     * @return a LayoutConstraintsDescriptor
     */
    public static LayoutConstraintsDescriptor createLayoutConstraintsDescriptor(LayoutConstraintsType layoutConstraints) {
        return LayoutConstraintsDescriptor.getLayoutConstraints(layoutConstraints.getPrefWidth(),
                layoutConstraints.getPrefHeight());
    }

}
