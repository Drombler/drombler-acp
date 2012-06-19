/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.application;

import org.richclientplatform.core.lib.util.Context;

/**
 *
 * @author puce
 */
public class Contexts {
    private static Context DEFAULT_CONTEXT;
    
    /*default*/ static void setDefault(Context defaultContext){
        DEFAULT_CONTEXT = defaultContext;
    }
//    public static Context getDefault(){
//        return DEFAULT_CONTEXT;
//    }
}
