/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.startup.main;

import java.util.Map;

/**
 *
 * @author puce
 */
public interface ApplicationConfigProvider {

    Map<String, String> getApplicationConfig();
}
