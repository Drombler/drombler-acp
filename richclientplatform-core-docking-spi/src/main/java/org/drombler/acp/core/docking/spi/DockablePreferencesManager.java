/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking.spi;

/**
 *
 * @author puce
 */
public interface DockablePreferencesManager<D> {

    DockablePreferences getDockablePreferences(D dockable);

    void registerDefaultDockablePreferences(Class<?> dockableClass, DockablePreferences dockablePreferences);
    
    void registerDockablePreferences(D dockable, DockablePreferences dockablePreferences);

    DockablePreferences unregisterDockablePreferences(D dockable);
    
    void reset();
}
