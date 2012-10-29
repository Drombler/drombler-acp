/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.action.jaxb.ToolBarsType;
import org.drombler.acp.core.application.ExtensionPoint;

/**
 *
 * @author puce
 */
@Component
@Service
public class ToolBarsExtensionPoint implements ExtensionPoint<ToolBarsType> {

//    private final List<MenusType> menusList = new ArrayList<>();
    @Override
    public Class<ToolBarsType> getJAXBRootClass() {
        return ToolBarsType.class;
    }
}
