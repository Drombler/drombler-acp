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
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.drombler.acp.core.docking.jaxb.DockingsType;
import org.drombler.acp.core.docking.spi.EditorDockingDescriptor;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.drombler.commons.docking.DockablePreferences;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author puce
 */
@Component(immediate = true)
@Reference(name = "editorDockingDescriptor", referenceInterface = EditorDockingDescriptor.class,
        cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)
public class EditorDockingHandler<D, DATA extends DockableData, E extends DockableEntry<D>> extends AbstractDockableDockingHandler<D, DATA, E> {

    private static final Logger LOG = LoggerFactory.getLogger(EditorDockingHandler.class);

    private final List<EditorDockingDescriptor<?>> unresolvedDockingDescriptors = new ArrayList<>();

    protected void bindEditorDockingDescriptor(EditorDockingDescriptor<?> dockingDescriptor) {
        resolveDockingDescriptor(dockingDescriptor);
    }

    protected void unbindEditorDockingDescriptor(EditorDockingDescriptor<?> dockingDescriptor) {
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
        dockingsType.getEditorDocking().forEach(dockingType -> {
            try {
                EditorDockingDescriptor<?> dockingDescriptor = EditorDockingDescriptor.createEditorDockingDescriptor(
                        dockingType, bundle);
                // TODO: register EditorDockingDescriptor as service? Omit resolveDockingDescriptor?
                resolveDockingDescriptor(dockingDescriptor);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        });
    }

    private void resolveDockingDescriptor(EditorDockingDescriptor<?> dockingDescriptor) {
        if (isInitialized()) {
            DATA dockableData = getDockableDataFactory().createDockableData(dockingDescriptor);
            registerClassDockableData(dockingDescriptor.getDockableClass(), dockableData);

            DockablePreferences dockablePreferences = createDockablePreferences(dockingDescriptor.getAreaId(), 0);
            registerDefaultDockablePreferences(dockingDescriptor.getDockableClass(), dockablePreferences);
        } else {
            unresolvedDockingDescriptors.add(dockingDescriptor);
        }
    }

    private void resolveUnresolvedDockables() {
        unresolvedDockingDescriptors.forEach(this::resolveDockingDescriptor);
    }
}
