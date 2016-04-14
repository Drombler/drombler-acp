package org.drombler.acp.core.data;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
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
@Repeatable(FileExtensions.class)
public @interface FileExtension {

    /**
     * The text to be displayed, e.g. as the text for filters in file dialogs. If the value starts with '%' the rest of the value is interpreted as a property key and the value gets looked-up in the
     * Bundle.properties file (or a locale * specific derivation of this file), which has to be in the same package as the annotated data type.
     *
     * @return the text to be displayed for this action
     */
    String displayName();

    String mimeType();

    String[] fileExtensions();

    /**
     * The position, e.g. to order the file extensions in filters of file dialogs. It's a best practice to leave out some positions between entries to allow other bundles to register entries between
     * some existing ones.
     *
     * @return the position
     */
    int position();
}
