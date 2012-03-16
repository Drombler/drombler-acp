/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.projectx.contactcenter.desktop.action.processing;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;

/**
 *
 * @author puce
 */
public interface ExtensionTrackerListener<T> {

    void addingExtension(Bundle bundle, BundleEvent event, T extension);

    //TODO: needed?
//    void modifiedExtension(Bundle bundle, BundleEvent event, T extension);
    void removedExtension(Bundle bundle, BundleEvent event, T extension);
}
