package org.drombler.acp.core.data;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.drombler.commons.context.LocalContextProvider;
import org.drombler.commons.data.DataCapabilityProvider;

/**
 * Registers a business object handler. A business object handler usually knows how to read, save etc. a particular kind of business object.
 *
 * To fully integrate with the framework it should implement {@link LocalContextProvider}, observe registered {@link DataCapabilityProvider}s and add the found data capabilities to it's local context.
 *
 * The easiest way to implement a business object handler is to extend {@link AbstractDataHandler}, but it's not required.
 *
 * @see AbstractDataHandler
 * @author puce
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface BusinessObjectHandler {

    /**
     * The icon name pattern to resolve the icons to be used for this file type handler.
     *
     * Note that this only specifies the name pattern. Drombler ACP Docking Framework looks for
     * <icon-base-name>16.<icon-extension> (expected to be 16x16 pixels). So if icon is &quot;test.png&quot;, the Drombler ACP Docking Framework would look for test16.png.
     *
     * @return the icon name pattern
     */
    String icon() default "";
}
