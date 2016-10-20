package org.drombler.acp.core.application;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Registers an extension.
 *
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
@Repeatable(Extensions.class)
public @interface Extension {

    /**
     * The extension file.
     *
     * @return the extension file
     */
    String extensionFile();

    Class<?> extensionJAXBRootClass();

}
