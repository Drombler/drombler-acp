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

import java.util.MissingResourceException;
import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.action.MenuItemSortingStrategy;
import org.drombler.acp.core.action.MenuItemSupplierFactory;
import org.drombler.acp.core.action.PositionSortingStrategy;
import org.drombler.acp.core.action.PositionableMenuItemAdapterFactory;
import org.drombler.acp.core.action.jaxb.MenuType;
import org.drombler.acp.core.commons.util.OSGiResourceBundleUtils;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A menu descriptor.
 *
 * @param <MenuItem> the GUI toolkit specific type for menu items
 * @param <F> the sorting strategy specific menu item supplier factory type
 * @author puce
 */
public class MenuDescriptor<MenuItem, F extends MenuItemSupplierFactory<MenuItem, F>> extends AbstractMenuEntryDescriptor<MenuItem, F> {

    private static final Logger LOG = LoggerFactory.getLogger(MenuDescriptor.class);

    private final String id;
    private final String displayName;
    private final MenuItemSortingStrategy<MenuItem, ?> sortingStrategy;

    /**
     * Creates a new instance of this class.
     *
     * @param id the id of this menu
     * @param displayName the display name of this menu. Typically the display name is localized.
     * @param path the path of menu ids of this menu
     * @param menuItemSupplierFactory a menu item supplier factory
     * @param sortingStrategy a sorting strategy
     */
    public MenuDescriptor(String id, String displayName, String path, F menuItemSupplierFactory, MenuItemSortingStrategy<MenuItem, ?> sortingStrategy) {
        super(path, menuItemSupplierFactory);
        this.id = id;
        this.displayName = displayName;
        this.sortingStrategy = sortingStrategy;
    }

//    private MenuRootDescriptor getRoot() {
////        MenuDescriptor root = this;
////        while (root.parent != null) {
////            root = root.parent;
////        }
//        return root;
//    }
//    private int getLevel() {
//        int level = 0;
//        MenuDescriptor parentMenuDescriptor = getParent();
//        while (parentMenuDescriptor != null) {
//            level++;
//            parentMenuDescriptor = parentMenuDescriptor.getParent();
//        }
//        return level;
//    }
//    /**
//     *
//     * @param path must be the path to this {@link MenuDescriptor} or a subpath.
//     * @return null if the path is the same as the current path else the first unresolved path id of the subpath
//     */
//    private String getUnresolvedPathId(String[] path) {
//        int level = getLevel();
//        if (path.length > level) {
//            return path[level];
//        } else {
//            return null;
//        }
//    }
//
//    private void registerMenuEntry(MenuEntryDescriptor menuEntryDescriptor) {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    private void registerUnresolvedMenuEntry(String unresolvedPathId, MenuEntryType menuEntry) {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    public List<Integer> getPathPositions() {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
    /**
     * Gets the display name. Typically the display name is localized.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the sorting strategy.
     *
     * @return the sorting strategy
     */
    public MenuItemSortingStrategy<MenuItem, ?> getSortingStrategy() {
        return sortingStrategy;
    }

    /**
     * Creates an instance of an {@link MenuDescriptor} from a {@link MenuType} unmarshalled from the application.xml.
     *
     * @param <MenuItem> the GUI toolkit specific type for menu items
     * @param menuType the unmarshalled MenuType
     * @param bundle the OSGi bundle
     * @return a MenuDescriptor
     */
    public static <MenuItem> MenuDescriptor<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> createMenuDescriptor(MenuType menuType, Bundle bundle) {
        try {
            return new MenuDescriptor<>(StringUtils.stripToNull(menuType.getId()),
                    OSGiResourceBundleUtils.getPackageResourceStringPrefixed(menuType.getPackage(),
                            menuType.getDisplayName(), bundle),
                    StringUtils.stripToEmpty(menuType.getPath()),
                    new PositionableMenuItemAdapterFactory<>(menuType.getPosition()),
                    new PositionSortingStrategy<>());
        } catch (MissingResourceException ex) {
            LOG.warn("ResourceBundle not found for menu {}: {}", menuType.getId(), ex.getMessage());
            throw ex;
        }
    }
//    private void registerMenu(MenuDescriptor menuDescriptor) {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    private void registerUnresolvedMenu(String unresolvedPathId, MenuType menu) {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    public static class MenuRootDescriptor extends MenuDescriptor {
//
//        private final ActionsMap actions;
//
//        private MenuRootDescriptor(ActionsMap actions) {
//            super(null, null, 0);
//            this.actions = actions;
////            this.root = this;
//        }
//
//        public void registerMenu(MenuType menu) {
//            String[] paths = menu.getPath().split("/", 0);
//
//            MenuDescriptor parentMenuDescriptor = getMenu(paths);
//            String unresolvedPathId = parentMenuDescriptor.getUnresolvedPathId(paths);
//
//            if (parentMenuDescriptor != null) {
//                MenuDescriptor menuDescriptor = new MenuDescriptor(menu.getId(), menu.getDisplayName(),
//                        menu.getPosition());
//                parentMenuDescriptor.registerMenu(menuDescriptor);
//
//            } else {
//                parentMenuDescriptor.registerUnresolvedMenu(unresolvedPathId, menu);
//            }
//        }
//
//        public void registerMenuEntry(MenuEntryType menuEntry) {
//            String[] paths = menuEntry.getPath().split("/", 0);
//
//            MenuDescriptor parentMenuDescriptor = getMenu(paths);
//            String unresolvedPathId = parentMenuDescriptor.getUnresolvedPathId(paths);
//
//            if (parentMenuDescriptor != null) {
//                MenuEntryDescriptor menuEntryDescriptor = new MenuEntryDescriptor(menuEntry.getActionId(),
//                       menuEntry.getPosition());
//                parentMenuDescriptor.registerMenuEntry(menuEntryDescriptor);
//
//            } else {
//                parentMenuDescriptor.registerUnresolvedMenuEntry(unresolvedPathId, menuEntry);
//            }
//        }
//
//        private MenuDescriptor getMenu(String[] paths) {
//            MenuDescriptor menuDescriptor = this;
//            for (String pathId : paths) {
//                MenuDescriptor nextMenuDescriptor = menuDescriptor.menuDescriptors.get(pathId);
//                if (nextMenuDescriptor != null) {
//                    menuDescriptor = nextMenuDescriptor;
//                } else {
//                    break;
//                }
//            }
//            return menuDescriptor;
//        }
//
////        private ActionDescriptor getAction(String actionId) {
////            return actions.getAction(actionId);
////        }
//    }
//
//    private class MenuContainerDescriptor {
//
//        private final Map<String, MenuDescriptor> menuDescriptors = new HashMap<>();
//
//
//        private void registerMenu(MenuDescriptor menuDescriptor) {
//            menuDescriptor.setParent(MenuDescriptor.this);
//            menuDescriptor.root = root;
//            menuDescriptors.put(menuDescriptor.id, menuDescriptor);
//            if (unresolvedMenus.containsKey(menuDescriptor.id)) {
//                for (MenuType unresolvedMenu : unresolvedMenus.get(menuDescriptor.id)) {
//                    root.registerMenu(unresolvedMenu);
//                }
//            }
//        }
//
//        private void registerUnresolvedMenu(String unresolvedPathId, MenuType menu) {
//            if (!unresolvedMenus.containsKey(unresolvedPathId)) {
//                unresolvedMenus.put(unresolvedPathId, new ArrayList<MenuType>());
//            }
//            unresolvedMenus.get(unresolvedPathId).add(menu);
//        }
//    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString() {
        return "MenuDescriptor[" + "id=" + id + ", displayName=" + displayName + ", path=" + getPath() + ", sortingStrategy=" + sortingStrategy + ']';
    }

}
