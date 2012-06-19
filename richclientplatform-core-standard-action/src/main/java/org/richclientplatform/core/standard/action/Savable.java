/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.standard.action;

import java.util.Locale;

/**
 *
 * @author puce
 */
//  TODO: implement Localizable from SoftSmithy? Else the Locale parameter of getDisplayString might be omitted 
// (and the default locale used instead)
public interface Savable {

    void save();

    String getDisplayString(Locale inLocale);
}
