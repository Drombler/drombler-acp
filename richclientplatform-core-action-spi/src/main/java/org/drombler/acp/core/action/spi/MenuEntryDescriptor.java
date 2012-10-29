/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi;

import org.apache.commons.lang.StringUtils;
import org.drombler.acp.core.action.jaxb.MenuEntryType;

/**
 *
 * @author puce
 */
public class MenuEntryDescriptor extends AbstractMenuEntryDescriptor {

    private final String actionId;

    public MenuEntryDescriptor(String actionId, String path, int position) {
        super(path, position);
        this.actionId = actionId;
    }

//    public ActionDescriptor getAction() {
//        return action;
//    }
    public String getActionId() {
        return actionId;
    }
    
//StringUtils.stripToNull(menuEntryType.getId()),StringUtils.stripToEmpty(menuEntryType.getPath()), menuEntryType.getPosition()

    public static MenuEntryDescriptor createMenuEntryDescriptor(MenuEntryType menuEntryType) {
        return new MenuEntryDescriptor(StringUtils.stripToNull(menuEntryType.getActionId()),
                StringUtils.stripToEmpty(menuEntryType.getPath()), menuEntryType.getPosition());
    }
}
