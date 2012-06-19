/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.lib.util;

/**
 *
 * @author puce
 */
//TODO: Good name? GlobalContextProvider?
//TODO: Good package/ bundle? SPI?
public interface ApplicationContextProvider {

    Context getApplicationContext();
}
