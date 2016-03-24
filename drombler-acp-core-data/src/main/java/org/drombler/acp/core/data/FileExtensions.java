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
@Target(ElementType.PACKAGE)
public @interface FileExtensions {

    /**
     * An array of the repeatable annotation {@link FileExtension}.
     *
     * @return an array of the repeatable annotation {@link FileExtension}
     */
    FileExtension[] value();
}
