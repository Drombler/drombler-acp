package org.drombler.acp.core.application;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Registers an extension using an extension file.<br>
 * <br>
 * An extension file is similar to the application file, but just contains a single extension point configuration.
 * <br>
 * Use this annotation if registration by file is more convenient than registration by annotations or if there are no corresponding annotations for the extension point.
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

    /**
     * The extension JAXB root class.
     *
     * @return the extension JAXB root class
     */
    Class<?> extensionJAXBRootClass();

}
