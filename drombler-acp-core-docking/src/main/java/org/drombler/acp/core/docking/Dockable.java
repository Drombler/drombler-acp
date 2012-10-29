/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking;

import org.drombler.acp.core.lib.util.context.Context;

/**
 *
 * @author puce
 */
// TODO: useful?
public interface Dockable {
//    String getTitle();

    void open();

    void requestActive();
    
    // TODO: needed?
    Context getContext();
}
