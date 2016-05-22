package org.drombler.acp.core.data;

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
public @interface BusinessObjectHandler {

    /**
     * The icon name pattern to resolve the icons to be used for this file type handler.
     *
     * Note that this only specifies the name pattern. Drombler ACP looks for
     * <icon-base-name>16.<icon-extension> for data types (expected to be 16x16 pixels). So if icon is &quot;test.png&quot;, Drombler ACP would look for test16.png.
     *
     * @return the icon name pattern
     */
    String icon() default "";
}
