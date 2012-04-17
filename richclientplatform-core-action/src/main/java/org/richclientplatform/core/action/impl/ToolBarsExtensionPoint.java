/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.richclientplatform.core.action.jaxb.ToolBarsType;
import org.richclientplatform.core.application.ExtensionPoint;

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
