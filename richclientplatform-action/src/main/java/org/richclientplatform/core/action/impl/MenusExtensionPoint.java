/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.richclientplatform.core.action.jaxb.MenusType;
import org.richclientplatform.core.application.ExtensionPoint;

/**
 *
 * @author puce
 */
@Component
@Service
public class MenusExtensionPoint implements ExtensionPoint<MenusType> {

//    private final List<MenusType> menusList = new ArrayList<>();

    @Override
    public Class<MenusType> getJAXBRootClass() {
        return MenusType.class;
    }

//    @Override
//    public void addingExtension(Bundle bundle, BundleEvent event, MenusType extension) {
//        menusList.add(extension);
//    }
//
//    @Override
//    public void removedExtension(Bundle bundle, BundleEvent event, MenusType extension) {
//        menusList.remove(extension);
//    }
}
