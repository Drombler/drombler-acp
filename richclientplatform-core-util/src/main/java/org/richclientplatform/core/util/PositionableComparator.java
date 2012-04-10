/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.util;

import java.util.Comparator;

/**
 *
 * @author puce
 */
public class PositionableComparator implements Comparator<Positionable> {

    @Override
    public int compare(Positionable o1, Positionable o2) {
        if (o1.getPosition() < o2.getPosition()) {
            return -1;
        } else if (o1.getPosition() > o2.getPosition()) {
            return 1;
        } else {
            return 0;
        }
    }
}
