/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.lib.util.context;

/**
 *
 * @author puce
 */
//TODO: Good package/ bundle? SPI?
public interface ActiveContextProvider {

    Context getActiveContext();
}