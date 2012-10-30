/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (Sourceforge.net user: puce).
 * Copyright 2012 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.core.action.spi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author puce
 */
public abstract class AbstractMenuEntryDescriptor {

    private static final String PATH_DELIMITER = "/";
    private final List<String> path;
    private final int position;

    protected AbstractMenuEntryDescriptor(String path, int position) {
        this.path = splitPath(path);
        this.position = position;
    }

    private List<String> splitPath(String path) {
        path = path.replaceFirst("^" + PATH_DELIMITER + "*", "");
        if (!path.equals("")) {
            return Arrays.asList(path.split("/"));
        } else {
            return Collections.emptyList();
        }
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
