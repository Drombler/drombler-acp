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
package org.drombler.acp.core.action.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.action.jaxb.ToolBarsType;
import org.drombler.acp.core.application.ExtensionPoint;

/**
 *
 * @author puce
 */
@Component
@Service
public class ToolBarsExtensionPoint implements ExtensionPoint<ToolBarsType> {

//    private final List<MenusType> menusList = new ArrayList<>();
    @Override
    public Class<ToolBarsType> getJAXBRootClass() {
        return ToolBarsType.class;
    }
}
