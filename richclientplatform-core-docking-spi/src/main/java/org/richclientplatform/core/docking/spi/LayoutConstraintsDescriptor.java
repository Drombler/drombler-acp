/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.richclientplatform.core.docking.spi;

import org.richclientplatform.core.docking.jaxb.LayoutConstraintsType;

/**
 *
 * @author puce
 */
public class LayoutConstraintsDescriptor {

    private double prefWidth;
    private double prefHeight;

    public static LayoutConstraintsDescriptor createLayoutConstraintsDescriptor(LayoutConstraintsType layoutConstraints) {
        LayoutConstraintsDescriptor layoutConstraintsDescriptor = new LayoutConstraintsDescriptor();
        layoutConstraintsDescriptor.setPrefWidth(layoutConstraints.getPrefWidth());
        layoutConstraintsDescriptor.setPrefHeight(layoutConstraints.getPrefHeight());
        return layoutConstraintsDescriptor;
    }

    /**
     * @return the prefWidth
     */
    public double getPrefWidth() {
        return prefWidth;
    }

    /**
     * @param prefWidth the prefWidth to set
     */
    public void setPrefWidth(double prefWidth) {
        this.prefWidth = prefWidth;
    }

    /**
     * @return the prefHeight
     */
    public double getPrefHeight() {
        return prefHeight;
    }

    /**
     * @param prefHeight the prefHeight to set
     */
    public void setPrefHeight(double prefHeight) {
        this.prefHeight = prefHeight;
    }
}
