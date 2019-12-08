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
package org.drombler.acp.core.action.spi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.drombler.acp.core.action.MenuItemSupplierFactory;

/**
 * A base class for menu entry descriptors.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <F> the sorting strategy specific menu item supplier factory type
 * @author puce
 */
public abstract class AbstractMenuEntryDescriptor<MenuItem, F extends MenuItemSupplierFactory<MenuItem, F>> {

    private static final String PATH_DELIMITER = "/";
    private final List<String> path;
    private final F menuItemSupplierFactory;

    /**
     * Creates a new instance of this class.
     *
     * @param path a slash '/' delimited path of menu IDs.
     * @param menuItemSupplierFactory the {@link MenuItemSupplierFactory}
     */
    protected AbstractMenuEntryDescriptor(String path, F menuItemSupplierFactory) {
        this.path = splitPath(path);
        this.menuItemSupplierFactory = menuItemSupplierFactory;
    }

    private List<String> splitPath(String path) {
        path = path.replaceFirst("^" + PATH_DELIMITER + "*", "");
        if (!path.equals("")) {
            return Arrays.asList(path.split("/"));
        } else {
            return Collections.emptyList();
        }
    }

//    public List<String> getPath() {
//        List<String> path = new ArrayList<>();
//        MenuDescriptor parentMenuDescriptor = getParent();
//        while (parentMenuDescriptor != null) {
//            path.add(parentMenuDescriptor.getId());
//            parentMenuDescriptor = parentMenuDescriptor.getParent();
//        }
//        Collections.reverse(path);
//        return path;
//    }

    /**
     * Gets the slash '/' delimited path of menu IDs.
     *
     * @return the path the slash '/' delimited path of menu IDs
     */
    public List<String> getPath() {
        return path;
    }

    /**
     * Gets the {@link MenuItemSupplierFactory}.
     *
     * @return the menuItemSupplierFactory
     */
    public F getMenuItemSupplierFactory() {
        return menuItemSupplierFactory;
    }
}
