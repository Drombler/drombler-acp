/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action;

/**
 *
 * @author puce
 */
//TODO extend EventListener?
//TODO E extend EventObject?
public interface ActionListener<E> {

    void actionPerformed(E event);
}
