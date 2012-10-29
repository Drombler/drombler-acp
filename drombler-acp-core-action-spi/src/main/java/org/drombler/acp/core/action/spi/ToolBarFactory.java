/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.action.spi;

/**
 *
 * @author puce
 */
public interface ToolBarFactory<T> {

    T createToolBar(ToolBarDescriptor toolBarDescriptor);
}
