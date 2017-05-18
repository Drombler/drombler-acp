package org.drombler.acp.core.status.spi.impl;

import org.drombler.acp.core.status.jaxb.StatusBarElementType;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 *
 * @author puce
 */
@Component

public class StatusBarElementHandler {

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindStatusBarElement(StatusBarElementType statusBarElement) {

    }

    public void unbindStatusBarElement(StatusBarElementType statusBarElement) {

    }
}
