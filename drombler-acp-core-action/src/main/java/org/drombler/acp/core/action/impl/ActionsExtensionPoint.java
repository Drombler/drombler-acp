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

import org.drombler.acp.core.action.jaxb.ActionsType;
import org.drombler.acp.core.application.ExtensionPoint;
import org.osgi.service.component.annotations.Component;

/**
 *
 * @author puce
 */
@Component
public class ActionsExtensionPoint implements ExtensionPoint<ActionsType> {

//    private final List<ActionsType> actionsList = new ArrayList<>();
    @Override
    public Class<ActionsType> getJAXBRootClass() {
        return ActionsType.class;
    }

//    @Override
//    public void addingExtension(Bundle bundle, BundleEvent event, ActionsType extension) {
//        actionsList.add(extension);
//    }
//
//    @Override
//    public void removedExtension(Bundle bundle, BundleEvent event, ActionsType extension) {
//        actionsList.remove(extension);
//    }
    
}
