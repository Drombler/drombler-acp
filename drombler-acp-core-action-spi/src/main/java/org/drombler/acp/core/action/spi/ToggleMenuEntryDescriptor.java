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

import org.apache.commons.lang3.StringUtils;
import org.drombler.acp.core.action.MenuItemSupplierFactory;
import org.drombler.acp.core.action.PositionableMenuItemAdapterFactory;
import org.drombler.acp.core.action.jaxb.ToggleMenuEntryType;

/**
 *
 * @author puce
 */
public class ToggleMenuEntryDescriptor<MenuItem, F extends MenuItemSupplierFactory<MenuItem>> extends MenuEntryDescriptor<MenuItem, F> {

    private final String toggleGroupId;

    public ToggleMenuEntryDescriptor(String actionId, String toggleGroupId, String path, F menuItemSupplierFactory) {
        super(actionId, path, menuItemSupplierFactory);
        this.toggleGroupId = toggleGroupId;
    }

    public ToggleMenuEntryDescriptor(String actionId, String path, F menuItemSupplierFactory) {
        this(actionId, null, path, menuItemSupplierFactory);
    }

    /**
     * @return the toggleGroupId
     */
    public String getToggleGroupId() {
        return toggleGroupId;
    }

    public static <MenuItem> ToggleMenuEntryDescriptor<MenuItem, PositionableMenuItemAdapterFactory<MenuItem>> createToggleMenuEntryDescriptor(ToggleMenuEntryType menuEntryType) {
        return new ToggleMenuEntryDescriptor<>(StringUtils.stripToNull(menuEntryType.getActionId()),
                StringUtils.stripToNull(menuEntryType.getToggleGroupId()),
                StringUtils.stripToEmpty(menuEntryType.getPath()),
                new PositionableMenuItemAdapterFactory<>(menuEntryType.getPosition()));
    }
}
