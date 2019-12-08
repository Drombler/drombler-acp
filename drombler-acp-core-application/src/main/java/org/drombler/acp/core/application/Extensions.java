package org.drombler.acp.core.application;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The containing annotation type for the repeatable annotation {@link Extension}.<br>
 * <br>
 * Note: Since {@link Extension} is a {@link Repeatable} annotation, this containing annotation type is usually not needed to be declared explicitly.
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
