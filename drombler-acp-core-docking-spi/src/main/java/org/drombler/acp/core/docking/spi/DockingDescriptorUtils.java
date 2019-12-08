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
 * A utility class for docking descriptors.
 *
 * @see ViewDockingDescriptor
 * @see EditorDockingDescriptor
 * @author puce
 */
public final class DockingDescriptorUtils {

    private DockingDescriptorUtils() {
    }

    /**
     * Creates an instance of a {@link ViewDockingDescriptor} from a {@link ViewDockingType} unmarshalled from the application.xml.
     *
     * @param viewDockingType the unmarshalled ViewDockingType
     * @param bundle the OSGi bundle
     * @return a ViewDockingDescriptor
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static ViewDockingDescriptor<?, ?, ?> createViewDockingDescriptor(ViewDockingType viewDockingType, Bundle bundle) throws
            ClassNotFoundException, InstantiationException, IllegalAccessException {
        final Class<?> dockableClass = loadClass(bundle, viewDockingType.getDockableClass());
        return createViewDockingDescriptor(viewDockingType, dockableClass);
    }

    private static <D, DATA extends DockableData, E extends DockableEntry<D, DATA>> ViewDockingDescriptor<D, DATA, E> createViewDockingDescriptor(ViewDockingType viewDocking,
            Class<D> dockableClass) throws ClassNotFoundException {
        String id = StringUtils.stripToNull(viewDocking.getId());
        String displayName = viewDocking.getDisplayName();
        String icon = StringUtils.stripToNull(viewDocking.getIcon());
        String areaId = StringUtils.stripToNull(viewDocking.getAreaId());
        String resourceBundleBaseName = viewDocking.getResourceBundleBaseName();
        String accelerator = viewDocking.getAccelerator();

        ViewDockingDescriptor<D, DATA, E> dockingDescriptor = new ViewDockingDescriptor<>(dockableClass, id, displayName, icon, accelerator, resourceBundleBaseName);
        dockingDescriptor.setAreaId(areaId);
        dockingDescriptor.setPosition(viewDocking.getPosition());
        dockingDescriptor.setActivateDockableMenuEntryDescriptor(new MenuEntryDescriptor(dockingDescriptor.getId(),
                getWindowPath(viewDocking), new PositionableMenuItemAdapterFactory<>(viewDocking.getMenuEntry().getPosition())));
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

    /**
     * Creates an instance of an {@link EditorDockingDescriptor} from an {@link EditorDockingType} unmarshalled from the application.xml.
     *
     * @param editorDockingType the unmarshalled EditorDockingType
     * @param bundle the OSGi bundle
     * @return an EditorDockingDescriptor
     * @throws ClassNotFoundException
     */
    public static EditorDockingDescriptor<?> createEditorDockingDescriptor(EditorDockingType editorDockingType, Bundle bundle)
            throws ClassNotFoundException {
        final Class<?> dockableClass = bundle.loadClass(StringUtils.stripToNull(editorDockingType.getDockableClass()));
        return createEditorDockingDescriptor(editorDockingType, dockableClass, bundle);
    }

    private static <D> EditorDockingDescriptor<D> createEditorDockingDescriptor(EditorDockingType docking, Class<D> dockableClass, Bundle bundle)
            throws ClassNotFoundException {
        String id = StringUtils.stripToNull(docking.getId());
        Class<?> contentType = loadClass(bundle, docking.getContentType());

        return new EditorDockingDescriptor<>(dockableClass, id, contentType);
    }


}
