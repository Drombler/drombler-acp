/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking;

import org.richclientplatform.core.lib.util.context.Context;

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
