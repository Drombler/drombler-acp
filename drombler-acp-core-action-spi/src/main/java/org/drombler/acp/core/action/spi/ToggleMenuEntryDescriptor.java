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

import org.apache.commons.lang.StringUtils;
import org.drombler.acp.core.action.jaxb.ToggleMenuEntryType;

/**
 *
 * @author puce
 */
public class ToggleMenuEntryDescriptor extends MenuEntryDescriptor {

    private final String toggleGroupId;

    public ToggleMenuEntryDescriptor(String actionId, String toggleGroupId, String path, int position) {
        super(actionId, path, position);
        this.toggleGroupId = toggleGroupId;
    }

    public ToggleMenuEntryDescriptor(String actionId, String path, int position) {
        this(actionId, null, path, position);
    }

    /**
     * @return the toggleGroupId
     */
    public String getToggleGroupId() {
        return toggleGroupId;
    }

    public static ToggleMenuEntryDescriptor createRadioMenuEntryDescriptor(ToggleMenuEntryType menuEntryType) {
        return new ToggleMenuEntryDescriptor(StringUtils.stripToNull(menuEntryType.getActionId()),
                StringUtils.stripToNull(menuEntryType.getToggleGroupId()),
                StringUtils.stripToEmpty(menuEntryType.getPath()), menuEntryType.getPosition());
    }
}
