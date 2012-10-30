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
package org.drombler.acp.core.docking.spi.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.acp.core.docking.jaxb.EditorDockingType;
import org.drombler.acp.core.docking.spi.EditorDockingDescriptor;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "editorDockingDescriptor", referenceInterface = EditorDockingDescriptor.class,
cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class EditorDockingHandler<A, D> extends AbstractDockableDockingHandler<A, D> {

    private final List<EditorDockingDescriptor> unresolvedDockingDescriptors = new ArrayList<>();

    protected void bindEditorDockingDescriptor(EditorDockingDescriptor dockingDescriptor) {
        resolveDockingDescriptor(dockingDescriptor);
    }

    protected void unbindEditorDockingDescriptor(EditorDockingDescriptor dockingDescriptor) {
    }

    @Activate
    protected void activate(ComponentContext context) {
        resolveUnresolvedDockables();
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
    }

    @Override
    protected void resolveDockingsType(DockingsType dockingsType, Bundle bundle, BundleContext context) {
        for (EditorDockingType dockingType : dockingsType.getEditorDocking()) {
            try {
                EditorDockingDescriptor dockingDescriptor = EditorDockingDescriptor.createEditorDockingDescriptor(
                        dockingType, bundle);
                resolveDockingDescriptor(dockingDescriptor);
            } catch (Exception ex) {
                Logger.getLogger(EditorDockingHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void resolveDockingDescriptor(EditorDockingDescriptor dockingDescriptor) {
        if (isInitialized()) {
            registerDockablePreferences(dockingDescriptor.getDockableClass(), dockingDescriptor.getAreaId(), 0);
        } else {
            unresolvedDockingDescriptors.add(dockingDescriptor);
        }
    }

    private void resolveUnresolvedDockables() {
        for (EditorDockingDescriptor unresolvedDockingDescriptor : unresolvedDockingDescriptors) {
            resolveDockingDescriptor(unresolvedDockingDescriptor);
        }
    }
}
