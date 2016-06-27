package org.drombler.acp.core.application;

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
public @interface Extensions {

    /**
     * An array of the repeatable annotation {@link Extension}.
     *
     * @return an array of the repeatable annotation {@link Extension}
     */
    Extension[] value();
}
