/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.lib.util;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author puce
 */
public class Positionables {

    private Positionables() {
    }

    public static <T extends Positionable> int getInsertionPoint(List<? extends T> list, T key) {
        PositionableComparator comparator = new PositionableComparator();
        int index = Collections.binarySearch(list, key, comparator);
        if (index < 0) {
            index = -index - 1;
        } else {
            for (T item : list.subList(index, list.size())) {
                if (comparator.compare(item, key) == 0) {
                    index++;
                } else {
                    break;
                }
            }
        }
        return index;
    }
}
