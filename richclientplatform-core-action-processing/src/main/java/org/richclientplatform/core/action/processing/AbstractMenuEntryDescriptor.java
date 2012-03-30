/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.action.processing;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author puce
 */
public abstract class AbstractMenuEntryDescriptor {

    private final List<String> path;
    private final int position;

    protected AbstractMenuEntryDescriptor(String path, int position) {
        this.path = Arrays.asList(path.split("/"));
        this.position = position;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }
//    public List<String> getPath() {
//        List<String> path = new ArrayList<>();
//        MenuDescriptor parentMenuDescriptor = getParent();
//        while (parentMenuDescriptor != null) {
//            path.add(parentMenuDescriptor.getId());
//            parentMenuDescriptor = parentMenuDescriptor.getParent();
//        }
//        Collections.reverse(path);
//        return path;
//    }

    /**
     * @return the path
     */
    public List<String> getPath() {
        return path;
    }
}
