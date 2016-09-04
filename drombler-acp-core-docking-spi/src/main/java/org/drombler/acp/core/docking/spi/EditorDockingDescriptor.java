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
package org.drombler.acp.core.docking.spi;

/**
 *
 * @author puce
 * @param <D> the type of the dockable
 */
public class EditorDockingDescriptor<D> extends AbstractDockableDockingDescriptor<D> {

    private final Class<?> contentType;

    public EditorDockingDescriptor(Class<D> dockableClass, String id, Class<?> contentType) {
        super(dockableClass, id);
        this.contentType = contentType;
    }

    /**
     * @return the contentType
     */
    public Class<?> getContentType() {
        return contentType;
    }


//    public D createEditor(Object content)
//            throws IllegalAccessException, SecurityException, InvocationTargetException, InstantiationException, IllegalArgumentException, NoSuchMethodException {
//        Constructor<? extends D> editorConstructor = getDockableClass().getConstructor(content.getClass());
//        return editorConstructor.newInstance(content);
//    }
}
