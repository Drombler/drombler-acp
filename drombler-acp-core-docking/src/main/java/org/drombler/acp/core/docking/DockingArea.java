/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.acp.core.docking;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
public @interface DockingArea {

    String id();

    DockingAreaKind kind() default DockingAreaKind.VIEW;

    int position();

    int[] path();

    boolean permanent() default false;

    LayoutConstraints layoutConstraints() default @LayoutConstraints;
}
