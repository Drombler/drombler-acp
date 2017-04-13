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
import org.drombler.acp.core.action.PositionableMenuItemAdapterFactory;
import org.drombler.acp.core.action.spi.MenuEntryDescriptor;
import static org.drombler.acp.core.commons.util.BundleUtils.loadClass;
import org.drombler.acp.core.docking.jaxb.EditorDockingType;
import org.drombler.acp.core.docking.jaxb.ViewDockingType;
import org.drombler.commons.docking.DockableData;
import org.drombler.commons.docking.DockableEntry;
import org.osgi.framework.Bundle;

/**
 *
 * @author puce
 */
public class DockingDescriptorUtils {

    public static ViewDockingDescriptor<?, ?, ?> createViewDockingDescriptor(ViewDockingType docking, Bundle bundle) throws
            ClassNotFoundException, InstantiationException, IllegalAccessException {
        final Class<?> dockableClass = loadClass(bundle, docking.getDockableClass());
        return createViewDockingDescriptor(docking, dockableClass);
    }

    private static <D, DATA extends DockableData, E extends DockableEntry<D, DATA>> ViewDockingDescriptor<D, DATA, E> createViewDockingDescriptor(ViewDockingType docking,
            Class<D> dockableClass) throws ClassNotFoundException {
        String id = StringUtils.stripToNull(docking.getId());
        String displayName = docking.getDisplayName();
        String icon = StringUtils.stripToNull(docking.getIcon());
        String areaId = StringUtils.stripToNull(docking.getAreaId());
        String resourceBundleBaseName = docking.getResourceBundleBaseName();
        String accelerator = docking.getAccelerator();

        ViewDockingDescriptor<D, DATA, E> dockingDescriptor = new ViewDockingDescriptor<>(dockableClass, id, displayName, icon, accelerator, resourceBundleBaseName);
        dockingDescriptor.setAreaId(areaId);
        dockingDescriptor.setPosition(docking.getPosition());
        dockingDescriptor.setActivateDockableMenuEntryDescriptor(new MenuEntryDescriptor(dockingDescriptor.getId(),
                getWindowPath(docking), new PositionableMenuItemAdapterFactory<>(docking.getMenuEntry().getPosition())));
        return dockingDescriptor;
    }

    private static String getWindowPath(ViewDockingType docking) {
        StringBuilder sb = new StringBuilder("Window");
        if (docking.getMenuEntry().getPath() != null) {
            sb.append("/");
            sb.append(docking.getMenuEntry().getPath());
        }
        return sb.toString();
    }

    public static EditorDockingDescriptor<?> createEditorDockingDescriptor(EditorDockingType docking, Bundle bundle)
            throws ClassNotFoundException {
        final Class<?> dockableClass = bundle.loadClass(StringUtils.stripToNull(docking.getDockableClass()));
        return createEditorDockingDescriptor(docking, dockableClass, bundle);
    }

    private static <D> EditorDockingDescriptor<D> createEditorDockingDescriptor(EditorDockingType docking, Class<D> dockableClass, Bundle bundle)
            throws ClassNotFoundException {
        String id = StringUtils.stripToNull(docking.getId());
        Class<?> contentType = loadClass(bundle, docking.getContentType());

        return new EditorDockingDescriptor<>(dockableClass, id, contentType);
    }


}
