/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.spi;

/**
 *
 * @author puce
 */
public interface DockablePreferencesManager<D> {

    DockablePreferences getDockablePreferences(D dockable);

    void registerDockablePreferences(D dockable, DockablePreferences dockablePreferences);

    DockablePreferences unregisterDockablePreferences(D dockable);
}
