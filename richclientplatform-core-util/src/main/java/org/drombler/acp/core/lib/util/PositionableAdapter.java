/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.lib.util;

/**
 *
 * @author puce
 */
public class PositionableAdapter<T> implements Positionable {


    private final T adapted;
    private final int position;

    public PositionableAdapter(T adapted, int position) {
        this.adapted = adapted;
        this.position = position;
    }

    public T getAdapted() {
        return adapted;
    }

    @Override
    public int getPosition() {
        return position;
    }

}
