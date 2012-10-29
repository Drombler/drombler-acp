/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi.impl;

/**
 * Empty config. Menus don't need any additional configurations.
 *
 * @author puce
 */
public class MenuConfig {

    private static final MenuConfig INSTANCE = new MenuConfig();

    private MenuConfig() {
    }

    public static MenuConfig getInstance() {
        return INSTANCE;
    }
}
