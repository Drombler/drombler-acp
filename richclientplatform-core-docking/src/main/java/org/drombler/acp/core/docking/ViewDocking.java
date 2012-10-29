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
@Target(ElementType.TYPE)
public @interface ViewDocking {

//    String id();
    String areaId();

    int position();

//    boolean singleton() default true;
    String displayName();

    String accelerator() default "";

    String icon() default "";

    DockingState state() default DockingState.DOCKED;

    WindowMenuEntry menuEntry();
}
