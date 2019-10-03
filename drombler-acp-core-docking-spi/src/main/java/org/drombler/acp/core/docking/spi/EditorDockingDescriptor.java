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
 * An editor docking descriptor.
 *
 * @author puce
 * @param <D> the type of the editor
 */
public class EditorDockingDescriptor<D> extends AbstractDockableDockingDescriptor<D> {

    private final Class<?> contentType;

    /**
     * Creates a new instance of this class.
     *
     * @param editorClass the type of the editor
     * @param id the id of the editor
     * @param contentType the content type of the editor
     */
    public EditorDockingDescriptor(Class<D> editorClass, String id, Class<?> contentType) {
        super(editorClass, id);
        this.contentType = contentType;
    }

    /**
     * Gets the content type of the editor.
     *
     * @return the content type of the editor
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
