/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.impl;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.drombler.acp.core.action.jaxb.ActionsType;
import org.drombler.acp.core.application.ExtensionPoint;

/**
 *
 * @author puce
 */
@Component
@Service
public class ActionsExtensionPoint implements ExtensionPoint<ActionsType> {

//    private final List<ActionsType> actionsList = new ArrayList<>();
    @Override
    public Class<ActionsType> getJAXBRootClass() {
        return ActionsType.class;
    }

//    @Override
//    public void addingExtension(Bundle bundle, BundleEvent event, ActionsType extension) {
//        actionsList.add(extension);
//    }
//
//    @Override
//    public void removedExtension(Bundle bundle, BundleEvent event, ActionsType extension) {
//        actionsList.remove(extension);
//    }
    
}
